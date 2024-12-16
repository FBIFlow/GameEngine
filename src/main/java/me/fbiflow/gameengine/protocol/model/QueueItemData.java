package me.fbiflow.gameengine.protocol.model;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record QueueItemData(UUID id, String[] players, Class<? extends AbstractGame> gameType) implements Serializable {

    public QueueItemData(UUID id,String[] players, Class<? extends AbstractGame> gameType) {
        this.id = id;
        this.players = players;
        this.gameType = gameType;
    }

    public QueueItemData(UUID id, List<Player> players, Class<? extends AbstractGame> gameType) {
        this(id, players.stream().map(Player::toString).toList().toArray(new String[players.size()]), gameType);
    }

    public QueueItemData(UUID id, Player[] players, Class<? extends AbstractGame> gameType) {
        this(id, (String[]) Arrays.stream(players).map(Player::getName).toArray(), gameType);
    }

    @Override
    public UUID id() {
        return this.id;
    }

    @Override
    public String[] players() {
        return this.players;
    }

    @Override
    public Class<? extends AbstractGame> gameType() {
        return this.gameType;
    }
}
