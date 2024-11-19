package me.fbiflow.remapped.protocol.packet.packets.server;

import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;

public class QueueStateUpdateEvent extends AbstractPacket {

    private final QueueState queueState;
    private final int timer;

    public QueueStateUpdateEvent(QueueState queueState, int timer) {
        this.queueState = queueState;
        this.timer = timer;
    }

    public QueueState getQueueState() {
        return this.queueState;
    }

    public int getTimer() {
        return this.timer;
    }

    enum QueueState {
        WAITING_FOR_PLAYERS,
        RUNNING_TIMER
    }
}