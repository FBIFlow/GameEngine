package me.fbiflow.remapped.core.controller;

import me.fbiflow.remapped.protocol.PacketHandler;
import me.fbiflow.remapped.protocol.PacketHolder;
import me.fbiflow.remapped.protocol.PacketListener;
import me.fbiflow.remapped.protocol.communication.SocketDataClient;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.PartyCreatePacket;
import me.fbiflow.remapped.util.LoggerUtil;

import java.net.Socket;

public class LobbyController implements PacketListener {

    private final LoggerUtil logger = new LoggerUtil(" | [LobbyController] -> ");

    private final SocketDataClient client;

    public LobbyController(SocketDataClient client) {
        this.client = client;
        startListener(client);
    }

    @PacketHandler
    private void onPartyCreatePacketReceive(PartyCreatePacket packet, Packet source, Socket sender) {

    }

    public SocketDataClient getConnection() {
        return this.client;
    }

    @Override
    public LoggerUtil getLogger() {
        return this.logger;
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