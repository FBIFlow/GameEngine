package me.fbiflow.gameengine.core.controller.lobby;

import me.fbiflow.gameengine.core.controller.event.EventHandleService;
import me.fbiflow.gameengine.core.controller.event.EventListener;
import me.fbiflow.gameengine.core.controller.event.EventProducer;
import me.fbiflow.gameengine.protocol.communication.Client;
import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.PacketHandleService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.handle.PacketProducer;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientRegisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientUnregisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.ClientRegisterResponsePacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.ClientUnregisterResponsePacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;

public class LobbyController implements PacketListener, EventListener {

    private final LoggerUtil logger = new LoggerUtil("| [LobbyController] ->");

    private final Client serverConnection;
    private final LobbyQueueController lobbyQueueController;

    private final PacketProducer packetProducer = PacketProducer.of(this);
    private final EventProducer eventProducer = EventProducer.of(this);

    private boolean registered;

    public LobbyController(String host, int port) {
        this.serverConnection = new Client(host, port, packetProducer);

        this.lobbyQueueController = new LobbyQueueController(this);

        PacketHandleService.getInstance().registerListener(packetProducer, this);
        EventHandleService.getInstance().registerListener(eventProducer, this);
    }

    public EventProducer getEventProducer() {
        return this.eventProducer;
    }

    public PacketProducer getPacketProducer() {
        return this.packetProducer;
    }

    public void start() {
        serverConnection.start();
        serverConnection.sendPacket(Packet.of(
                new ClientRegisterPacket(serverConnection.getClientId(), ClientType.LOBBY_CONTROLLER, null)
        ));
        logger.log("Sent enable packet");
    }

    public void stop() {
        serverConnection.sendPacket(Packet.of(
                new ClientUnregisterPacket(ClientType.LOBBY_CONTROLLER)
        ));
        logger.log("Sent disable packet");
    }

    public Client getConnection() {
        System.err.println("time when calling getConnection: " + System.currentTimeMillis());
        return this.serverConnection;
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