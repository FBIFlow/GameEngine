package me.fbiflow.gameengine.protocol.packet.packets.party;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

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

    @Override
    public String toString() {
        return "PartyInviteAddPacket{" +
                "sender=" + sender +
                ", invited=" + invited +
                '}';
    }
}