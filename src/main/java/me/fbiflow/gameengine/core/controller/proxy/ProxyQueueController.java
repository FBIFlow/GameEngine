package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.core.model.QueueManager;
import me.fbiflow.gameengine.core.model.WrappedQueueItem;
import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.protocol.handle.PacketHandleService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.queue.PlayerQueueJoinRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.queue.PlayerQueueLeaveRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.queue.QueueItemUpdatePacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.session.SessionGetRequestPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;

public class ProxyQueueController implements PacketListener {

    private final QueueManager queueManager;

    private final ProxyController proxy;

    private final LoggerUtil logger = new LoggerUtil("| [ProxyQueueController] ->");

    public ProxyQueueController(ProxyController proxyController) {
        this.queueManager = new QueueManager();
        this.proxy = proxyController;
        PacketHandleService.getInstance().registerListener(proxyController.getPacketProducer(), this);
    }

    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    @PacketHandler
    private void onPlayerQueueJoinRequestPacketReceive(PlayerQueueJoinRequestPacket packet, Packet source, Socket sender) {
        WrappedQueueItem wrappedQueueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
        if (wrappedQueueItem.queueItem == null) {
            return;
        }
        var response = new QueueItemUpdatePacket(wrappedQueueItem.queueItem);
        proxy.sendPacket(sender, Packet.of(response));
    }

    @PacketHandler
    private void onPlayerQueueLeaveRequestPacketReceive(PlayerQueueLeaveRequestPacket packet, Packet source, Socket sender) {
        queueManager.leaveQueue(packet.getWhoLeaves());
    }

    private void findFreeSession(Class<? extends AbstractGame> gameType) {
        System.err.println("Called find free session");
        var packet = new SessionGetRequestPacket(gameType);

    }

}