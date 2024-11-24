package me.fbiflow.gameengine.core.controller;

import me.fbiflow.gameengine.core.model.SessionHolder;
import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.protocol.PacketHandler;
import me.fbiflow.gameengine.protocol.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataClient;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.session.SessionFoundPacket;
import me.fbiflow.gameengine.protocol.packet.packets.session.SessionGetRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.session.SessionNotFoundPacket;
import me.fbiflow.gameengine.util.LoggerUtil;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionController extends PacketListener {

    private final LoggerUtil logger = new LoggerUtil(" | [SessionController] -> ");

    private final List<SessionHolder> sessionHolders;

    private final SocketDataClient server;

    public SessionController(String host, int port, List<SessionHolder> sessionHolders) {
        this.sessionHolders = sessionHolders;
        this.server = new SocketDataClient(host, port);
        startListener(server);
    }

    @PacketHandler
    private void onSessionGetRequest(SessionGetRequestPacket packet, Packet source, Socket sender) {
        UUID responseId =  packet.getPacketId();
        var gameType = packet.getGameType();
        List<SessionHolder> freeSessions = getFreeSessions(gameType);
        if (freeSessions == null) {
            server.sendPacket(Packet.of(new SessionNotFoundPacket(responseId)));
            return;
        }
        server.sendPacket(Packet.of(new SessionFoundPacket(responseId, freeSessions.getFirst().getId())));
    }

    public @Nullable List<SessionHolder> getFreeSessions(Class<? extends AbstractGame> gameType) {
        List<SessionHolder> freeSessions = new ArrayList<>();
        for (SessionHolder sessionHolder : this.sessionHolders) {
            if (!sessionHolder.getAllowedGameTypes().contains(gameType)) {
                continue;
            }
            freeSessions.add(sessionHolder);
        }
        return freeSessions.isEmpty() ? null : freeSessions;
    }

}