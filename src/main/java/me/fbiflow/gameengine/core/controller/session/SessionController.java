package me.fbiflow.gameengine.core.controller.session;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.game.GameManager;
import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.PacketHandleService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.communication.Client;
import me.fbiflow.gameengine.protocol.handle.PacketProducer;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientRegisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientUnregisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.session.SessionFoundPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.ClientRegisterResponsePacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.ClientUnregisterResponsePacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.session.SessionGetCallbackRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.session.SessionGetRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.session.SessionNotFoundPacket;
import me.fbiflow.gameengine.util.LoggerUtil;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.*;

public class SessionController implements PacketListener {

    private final LoggerUtil logger = new LoggerUtil("| [SessionController] ->");

    private final List<SessionHolder> sessionHolders;
    private final Client serverConnection;
    private final Map<String, Integer> allowedGames;

    private final PacketProducer packetProducer = PacketProducer.of(this);

    private boolean registered;

    public SessionController(String host, int port, List<SessionHolder> sessionHolders) {
        this.serverConnection = new Client(host, port, packetProducer);
        this.sessionHolders = sessionHolders;
        this.allowedGames = new HashMap<>();
        sessionHolders.forEach(sessionHolder -> {
            sessionHolder.getAllowedGameTypes().forEach(gameType -> {
                this.allowedGames.put(GameManager.getId(gameType), GameManager.hashCode(gameType));
            });
        });

        PacketHandleService.getInstance().registerListener(packetProducer, this);
    }

    public void start() {
        serverConnection.start();
        serverConnection.sendPacket(Packet.of(
                new ClientRegisterPacket(serverConnection.getClientId(), ClientType.SESSION_CONTROLLER, allowedGames)
        ));
    }

    public void stop() {
        serverConnection.sendPacket(Packet.of(
                new ClientUnregisterPacket(ClientType.SESSION_CONTROLLER)
        ));
    }

    protected @Nullable SessionHolder getFreeSession(Class<? extends AbstractGame> gameType) {
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
    protected @Nullable List<SessionHolder> getFreeSessions(Class<? extends AbstractGame> gameType) {
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

    @PacketHandler
    private void onSessionGetRequest(SessionGetRequestPacket packet, Packet source, Socket sender) {
        UUID responseId =  packet.getPacketId();
        var gameType = packet.getGameType();
        SessionHolder freeSession = getFreeSession(gameType);
        if (freeSession == null) {
            serverConnection.sendPacket(Packet.of(new SessionNotFoundPacket(responseId)));
            return;
        }
        freeSession.reserve();
        serverConnection.sendPacket(Packet.of(new SessionFoundPacket(responseId, freeSession.getId())));
    }

    @PacketHandler
    private void onSessionGetCallbackRequest(SessionGetCallbackRequestPacket packet, Packet source, Socket sender) {
        
    }

    @PacketHandler
    private void onClientRegisterResponsePacketReceive(ClientRegisterResponsePacket packet, Packet source, Socket sender) {
        logger.log("Received client registering accept from proxy controller");
        this.registered = true;
    }

    @PacketHandler
    private void onClientUnRegisterResponsePacketReceive(ClientUnregisterResponsePacket packet, Packet source, Socket sender) {
        logger.log("Received client unregistering packet from proxy controller");
        this.registered = false;
    }
}