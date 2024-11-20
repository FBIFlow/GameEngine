package me.fbiflow.remapped.protocol.packet.packets;

import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

public class PartyLeavePacket extends AbstractPacket {

    private final Player whoLeaves;

    public PartyLeavePacket(Player whoLeaves) {
        this.whoLeaves = whoLeaves;
    }

    public Player getWhoLeaves() {
        return this.whoLeaves;
    }
}