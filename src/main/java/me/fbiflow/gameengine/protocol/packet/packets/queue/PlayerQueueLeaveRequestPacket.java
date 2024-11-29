package me.fbiflow.gameengine.protocol.packet.packets.queue;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

public class PlayerQueueLeaveRequestPacket extends AbstractPacket {

    private final Player whoLeaves;

    public PlayerQueueLeaveRequestPacket(Player whoLeaves) {
        this.whoLeaves = whoLeaves;
    }

    public Player getWhoLeaves() {
        return this.whoLeaves;
    }

    @Override
    public String toString() {
        return "PlayerQueueLeaveRequestPacket{" +
                "whoLeaves=" + whoLeaves +
                '}';
    }
}