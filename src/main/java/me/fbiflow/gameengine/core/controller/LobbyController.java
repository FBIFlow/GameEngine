package me.fbiflow.gameengine.core.controller;

import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataClient;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.ClientRegisterPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

public class LobbyController implements PacketListener {

    private final LoggerUtil logger = new LoggerUtil(" | [LobbyController] -> ");

    private final SocketDataClient client;

    public LobbyController(SocketDataClient client) {
        this.client = client;
        CallbackService.getInstance().registerListener(client.getPacketProducer(), this);
        client.sendPacket(
                Packet.of(new ClientRegisterPacket(ClientType.LOBBY_CONTROLLER))
        );
    }

    public SocketDataClient getConnection() {
        return this.client;
    }
}