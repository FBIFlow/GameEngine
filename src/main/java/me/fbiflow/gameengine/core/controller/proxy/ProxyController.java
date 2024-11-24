package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.protocol.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataServer;
import me.fbiflow.gameengine.util.LoggerUtil;

public class ProxyController extends PacketListener {

    private final SocketDataServer server;

    private final QueueController queueController;
    private final PartyController partyController;

    private final LoggerUtil logger = new LoggerUtil(" | [ProxyController] -> ");

    public ProxyController(SocketDataServer server) {
        this.queueController = new QueueController(server);
        this.partyController = new PartyController(server);

        this.server = server;
        startListener(server);

    }
}