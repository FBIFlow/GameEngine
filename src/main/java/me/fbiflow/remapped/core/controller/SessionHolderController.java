package me.fbiflow.remapped.core.controller;

import me.fbiflow.remapped.core.model.SessionUnit;
import me.fbiflow.remapped.core.model.game.AbstractGame;
import me.fbiflow.remapped.protocol.PacketHandler;
import me.fbiflow.remapped.protocol.PacketHolder;
import me.fbiflow.remapped.protocol.PacketListener;
import me.fbiflow.remapped.protocol.communication.SocketDataClient;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.SessionFoundPacket;
import me.fbiflow.remapped.protocol.packet.packets.SessionGetRequestPacket;
import me.fbiflow.remapped.protocol.packet.packets.SessionNotFoundPacket;
import me.fbiflow.remapped.util.LoggerUtil;

import java.net.Socket;
import java.util.List;

public class SessionHolderController implements PacketListener {

    private final LoggerUtil logger = new LoggerUtil(" | [SessionHolder] ->");

    private final List<SessionUnit> sessionUnits;

    private final SocketDataClient server;

    public SessionHolderController(SocketDataClient server, List<SessionUnit> sessionUnits) {
        this.sessionUnits = sessionUnits;
        this.server = server;
        startListener(server);
    }

    @PacketHandler
    private void onGetSessionRequestPacketReceive(SessionGetRequestPacket packet, Packet source, Socket sender) {
        SessionUnit sessionUnit = getFreeSession(packet.getGameType());
        if (sessionUnit == null) {
            //TODO: Could not to find session, ask other
            server.sendPacket(Packet.of(new SessionNotFoundPacket(packet.getPacketId())));
            return;
        }
        //FOUND FREE SESSION
        server.sendPacket(Packet.of(new SessionFoundPacket(packet.getPacketId())));
    }

    private SessionUnit getFreeSession(Class<? extends AbstractGame> gameType) {
        return null;
    }

    @Override
    public LoggerUtil getLogger() {
        return null;
    }

    @Override
    public void handlePacket(Packet source, Socket sender) {
        PacketListener.super.handlePacket(source, sender);
    }

    @Override
    public void startListener(PacketHolder packetHolder) {
        PacketListener.super.startListener(packetHolder);
    }
}