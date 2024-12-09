package me.fbiflow.gameengine.core.controller;

import me.fbiflow.gameengine.protocol.communication.SocketDataClient;
import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientRegisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientUnregisterPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

public class LobbyController implements PacketListener {

    private final LoggerUtil logger = new LoggerUtil(" | [LobbyController] -> ");

    private final SocketDataClient serverConnection;

    public LobbyController(SocketDataClient serverConnection) {
        this.serverConnection = serverConnection;
        CallbackService.getInstance().registerListener(serverConnection.getPacketProducer(), this);
    }

    public void start() {
        serverConnection.sendPacket(Packet.of(
                new ClientRegisterPacket(ClientType.LOBBY_CONTROLLER, null)
        ));
    }

    public void stop() {
        serverConnection.sendPacket(Packet.of(
                new ClientUnregisterPacket(ClientType.LOBBY_CONTROLLER)
        ));
        logger.log("Disabled");
    }

    public SocketDataClient getConnection() {
        return this.serverConnection;
    }
}