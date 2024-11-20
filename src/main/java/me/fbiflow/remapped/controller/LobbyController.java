package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.protocol.communication.SocketDataClient;
import me.fbiflow.remapped.protocol.packet.Packet;

public class LobbyController {





    private final SocketDataClient client;

    public LobbyController(SocketDataClient client) {
        this.client = client;
    }

    public void sendPacket(Packet packet) {
        client.sendPacket(packet);
    }
}