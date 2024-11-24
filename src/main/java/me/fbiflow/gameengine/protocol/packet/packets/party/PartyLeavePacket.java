package me.fbiflow.gameengine.protocol.packet.packets.party;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

public class PartyLeavePacket extends AbstractPacket {

    private final Player whoLeaves;

    public PartyLeavePacket(Player whoLeaves) {
        this.whoLeaves = whoLeaves;
    }

    public Player getWhoLeaves() {
        return this.whoLeaves;
    }
}