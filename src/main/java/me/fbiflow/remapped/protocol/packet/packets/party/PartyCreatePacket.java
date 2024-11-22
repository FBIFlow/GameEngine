package me.fbiflow.remapped.protocol.packet.packets.party;

import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

public class PartyCreatePacket extends AbstractPacket {

    private final Player owner;

    public PartyCreatePacket(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }
}