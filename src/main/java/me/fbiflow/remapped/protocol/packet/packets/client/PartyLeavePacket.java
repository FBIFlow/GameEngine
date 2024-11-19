package me.fbiflow.remapped.protocol.packet.packets.client;

import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;

public class PartyLeavePacket extends AbstractPacket {

    private final Player whoLeaves;

    public PartyLeavePacket(Player whoLeaves) {
        this.whoLeaves = whoLeaves;
    }

    public Player getWhoLeaves() {
        return this.whoLeaves;
    }
}