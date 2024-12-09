package me.fbiflow.gameengine.core.controller;

import me.fbiflow.gameengine.core.model.SessionHolder;
import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataClient;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientRegisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientUnregisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.session.SessionFoundPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.session.SessionGetCallbackRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.session.SessionGetRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.session.SessionNotFoundPacket;
import me.fbiflow.gameengine.util.LoggerUtil;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionController implements PacketListener {

    private final LoggerUtil logger = new LoggerUtil("| [SessionController] ->");

    private final List<SessionHolder> sessionHolders;

    private final SocketDataClient serverConnection;

    public SessionController(SocketDataClient serverConnection, List<SessionHolder> sessionHolders) {
        this.serverConnection = serverConnection;
        this.sessionHolders = sessionHolders;

        CallbackService.getInstance().registerListener(serverConnection.getPacketProducer(), this);
    }

    public void start() {
        serverConnection.sendPacket(Packet.of(
                new ClientRegisterPacket(ClientType.SESSION_CONTROLLER)
        ));
    }

    public void stop() {
        serverConnection.sendPacket(Packet.of(
                new ClientUnregisterPacket(ClientType.SESSION_CONTROLLER)
        ));
    }

    @PacketHandler
    private void onSessionGetRequest(SessionGetRequestPacket packet, Packet source, Socket sender) {
        UUID responseId =  packet.getPacketId();
        var gameType = packet.getGameType();
        SessionHolder freeSession = getFreeSession(gameType);
        if (freeSession == null) {
            serverConnection.sendPacket(Packet.of(new SessionNotFoundPacket(responseId)));
            return;
        }
        freeSession.registerGame(gameType);
        serverConnection.sendPacket(Packet.of(new SessionFoundPacket(responseId, freeSession.getId())));
    }

    @PacketHandler
    private void onSessionGetCallbackRequest(SessionGetCallbackRequestPacket packet, Packet source, Socket sender) {
        
    }

    public @Nullable SessionHolder getFreeSession(Class<? extends AbstractGame> gameType) {
        for (SessionHolder sessionHolder: this.sessionHolders) {
            if (!sessionHolder.getAllowedGameTypes().contains(gameType)) {
                continue;
            }
            if (sessionHolder.isBusy()) {
                continue;
            }
            return sessionHolder;
        }
        return null;
    }

    @Deprecated(forRemoval = true)
    public @Nullable List<SessionHolder> getFreeSessions(Class<? extends AbstractGame> gameType) {
        List<SessionHolder> freeSessions = new ArrayList<>();
        for (SessionHolder sessionHolder : this.sessionHolders) {
            if (!sessionHolder.getAllowedGameTypes().contains(gameType)) {
                continue;
            }
            if(sessionHolder.isBusy()) {
                continue;
            }
            freeSessions.add(sessionHolder);
        }
        return freeSessions.isEmpty() ? null : freeSessions;
    }
}