package me.fbiflow.remapped.protocol.packet.packets;

import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.wrapper.internal.Player;

public class PlayerQueueJoinRequestPacket extends AbstractPacket {

    private final Player whoJoins;
    private final Class<? extends AbstractGame> gameType;

    public PlayerQueueJoinRequestPacket(Player whoJoins, Class<? extends AbstractGame> gameType) {
        this.whoJoins = whoJoins;
        this.gameType = gameType;
    }

    public Player getWhoJoins() {
        return this.whoJoins;
    }

    public Class<? extends AbstractGame> getGameType() {
        return this.gameType;
    }
}