package me.fbiflow.remapped.protocol.packet.packets;

import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

public class PlayerQueueLeaveRequestPacket extends AbstractPacket {

    private final Player whoLeaves;

    public PlayerQueueLeaveRequestPacket(Player whoLeaves) {
        this.whoLeaves = whoLeaves;
    }

    public Player getWhoLeaves() {
        return this.whoLeaves;
    }
}