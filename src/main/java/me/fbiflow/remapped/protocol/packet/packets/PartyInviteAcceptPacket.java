package me.fbiflow.remapped.protocol.packet.packets;

import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

public class PartyInviteAcceptPacket extends AbstractPacket {

    private final Player sender;
    private final Player invited;


    public PartyInviteAcceptPacket(Player sender, Player invited) {
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
