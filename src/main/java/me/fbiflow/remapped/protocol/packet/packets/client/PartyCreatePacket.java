package me.fbiflow.remapped.protocol.packet.packets.client;

import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;

public class PartyCreatePacket extends AbstractPacket {

    private final Player owner;

    public PartyCreatePacket(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }
}