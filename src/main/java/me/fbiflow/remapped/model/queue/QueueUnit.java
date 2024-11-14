package me.fbiflow.remapped.model.queue;

import me.fbiflow.remapped.model.game.Game;
import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueueUnit {

    private final UUID uuid;
    private final List<Player> players;

    private Class<? extends Game> gameType;
    private Player owner;

    {
        this.uuid = UUID.randomUUID();
        this.players = new ArrayList<>();
    }

    protected QueueUnit(Class<? extends Game> gameType, Player owner) {
        this.gameType = gameType;
        this.owner = owner;
    }

    protected QueueUnit(Class<? extends Game> gameType) {
        this.gameType = gameType;
    }

    protected QueueUnit(Player owner) {
        this.owner = owner;
    }

    protected QueueUnit() {

    }

    protected UUID getUuid() {
        return this.uuid;
    }

    protected List<Player> getPlayers() {
        return this.players;
    }

    protected Class<? extends Game> getGameType() {
        return this.gameType;
    }

    protected void setGameType(Class<? extends Game> gameType) {
        this.gameType = gameType;
    }

    protected Player getOwner() {
        return this.owner;
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
    }
}
