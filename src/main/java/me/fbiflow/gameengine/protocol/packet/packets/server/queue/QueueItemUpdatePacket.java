package me.fbiflow.gameengine.protocol.packet.packets.server.queue;

import me.fbiflow.gameengine.core.model.QueueItem;
import me.fbiflow.gameengine.protocol.model.QueueItemData;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

public class QueueItemUpdatePacket extends AbstractPacket {

    private final QueueItemData queueItemData;

    public QueueItemUpdatePacket(QueueItem queueItem) {
        this.queueItemData = new QueueItemData(queueItem.getUuid(), queueItem.getMembersCopy(), queueItem.getGameType());
    }

    public QueueItemData getQueueItemData() {
        return this.queueItemData;
    }
}