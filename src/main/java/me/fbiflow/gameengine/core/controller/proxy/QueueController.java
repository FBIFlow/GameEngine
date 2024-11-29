package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.core.model.QueueItem;
import me.fbiflow.gameengine.core.model.QueueManager;
import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.protocol.communication.*;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.queue.PlayerQueueJoinRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.queue.PlayerQueueLeaveRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.session.SessionGetRequestPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;

public class QueueController implements PacketListener {

    private final QueueManager queueManager;

    private final ProxyController proxyController;
    private final SocketDataServer server;

    private final LoggerUtil logger = new LoggerUtil("| [QueueController] ->");

    public QueueController(ProxyController proxyController) {
        this.queueManager = new QueueManager();
        this.proxyController = proxyController;
        this.server = proxyController.getServer();
        CallbackService.getInstance().registerListener(this.server.getPacketProducer(), this);
    }

    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    @PacketHandler
    private void onPlayerQueueJoinRequestPacketReceive(PlayerQueueJoinRequestPacket packet, Packet source, Socket sender) {
        QueueItem queueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
        if (queueItem.isValid()) {
            findFreeSession(queueItem.getGameType());
        }
    }

    @PacketHandler
    private void onPlayerQueueLeaveRequestPacketReceive(PlayerQueueLeaveRequestPacket packet, Packet source, Socket sender) {
        queueManager.leaveQueue(packet.getWhoLeaves());
    }

    private void findFreeSession(Class<? extends AbstractGame> gameType) {
        var packet = new SessionGetRequestPacket(gameType);

    }

}