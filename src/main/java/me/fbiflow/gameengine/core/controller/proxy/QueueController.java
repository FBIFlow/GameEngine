package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.core.model.QueueItem;
import me.fbiflow.gameengine.core.model.QueueManager;
import me.fbiflow.gameengine.protocol.PacketHandler;
import me.fbiflow.gameengine.protocol.PacketHolder;
import me.fbiflow.gameengine.protocol.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.queue.PlayerQueueJoinRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.queue.PlayerQueueLeaveRequestPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;

public class QueueController extends PacketListener {

    private final QueueManager queueManager;

    private PacketHolder server;

    private final LoggerUtil logger = new LoggerUtil(" | [QueueService] ->");

    public QueueController(PacketHolder server) {
        this.queueManager = new QueueManager();
        this.server = server;
        startListener(this.server);
    }

    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    @PacketHandler
    private void onPlayerQueueJoin(PlayerQueueJoinRequestPacket packet, Packet source, Socket sender) {
        QueueItem queueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
    }

    @PacketHandler
    private void onPlayerQueueLeave(PlayerQueueLeaveRequestPacket packet, Packet source, Socket sender) {
        queueManager.leaveQueue(packet.getWhoLeaves());
    }
}