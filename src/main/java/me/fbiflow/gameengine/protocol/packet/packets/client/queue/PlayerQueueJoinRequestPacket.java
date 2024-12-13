package me.fbiflow.gameengine.protocol.packet.packets.client.queue;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

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

    @Override
    public String toString() {
        return "PlayerQueueJoinRequestPacket{" +
                "whoJoins=" + whoJoins +
                ", gameType=" + gameType +
                '}';
    }
}