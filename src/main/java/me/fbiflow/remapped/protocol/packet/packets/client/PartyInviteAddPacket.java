package me.fbiflow.remapped.protocol.packet.packets.client;

import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;

public class PartyInviteAddPacket extends AbstractPacket {

    private final Player sender;
    private final Player invited;

    public PartyInviteAddPacket(Player sender, Player invited) {
        this.sender = sender;
        this.invited = invited;
    }

    public Player getSender() {
        return this.sender;
    }

    public Player getInvited() {
        return this.invited;
    }
}