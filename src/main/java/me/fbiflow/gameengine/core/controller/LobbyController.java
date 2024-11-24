package me.fbiflow.gameengine.core.controller;

import me.fbiflow.gameengine.protocol.PacketHandler;
import me.fbiflow.gameengine.protocol.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataClient;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.party.PartyCreatePacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;

public class LobbyController extends PacketListener {

    private final LoggerUtil logger = new LoggerUtil(" | [LobbyController] -> ");

    private final SocketDataClient client;

    public LobbyController(SocketDataClient client) {
        this.client = client;
        startListener(client);
    }

    @PacketHandler
    private void onPartyCreatePacketReceive(PartyCreatePacket packet, Packet source, Socket sender) {

    }

    @Deprecated(forRemoval = true)
    public SocketDataClient getConnection() {
        return this.client;
    }
}