package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.protocol.communication.SocketDataServer;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.ClientRegisterPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;

public class ProxyController implements PacketListener {

    private final SocketDataServer server;
    private final LoggerUtil logger = new LoggerUtil("| [ProxyController] ->");
    private QueueController queueController;
    private PartyController partyController;

    public ProxyController(SocketDataServer server) {
        this.server = server;
        CallbackService.getInstance().registerListener(this.server.getPacketProducer(), this);
    }

    protected SocketDataServer getServer() {
        return this.server;
    }

    public void start() {
        this.queueController = new QueueController(this);
        this.partyController = new PartyController(this);
        server.start();
    }

    @PacketHandler
    private void onClientRegisterReceive(ClientRegisterPacket packet, Packet source, Socket sender) {

    }

}