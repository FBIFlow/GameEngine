package me.fbiflow.gameengine.protocol.packet.packets.client.party;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

public class PartyCreatePacket extends AbstractPacket {

    private final Player owner;

    public PartyCreatePacket(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }

    @Override
    public String toString() {
        return "PartyCreatePacket{" +
                "owner=" + owner +
                '}';
    }
}