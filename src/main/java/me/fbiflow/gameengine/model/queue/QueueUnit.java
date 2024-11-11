package me.fbiflow.gameengine.model.queue;

import me.fbiflow.gameengine.model.game.Game;
import me.fbiflow.gameengine.model.wrapper.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class QueueUnit {

    private Class<? extends Game> gameType;
    private Player owner;
    private final List<Player> players;

    private int maxPlayers = -1;

    public QueueUnit(Class<? extends Game> gameType) {
        this.gameType = gameType;
        this.players = new ArrayList<>(getMaxPlayers());
    }

    public QueueUnit(Class<? extends Game> gameType, Player owner) {
        this.gameType = gameType;
        this.owner = owner;
        this.players = new ArrayList<>(getMaxPlayers());
    }

    public Class<? extends Game> getGameType() {
        return this.gameType;
    }

    public boolean hasOwner() {
        return this.owner != null;
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getPlayersCount() {
        return this.players.size();
    }

    public boolean hasFreeSlot() {
        return getPlayersCount() < getMaxPlayers();
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    public void addPlayer(Player player) {
        if (getPlayersCount() == getMaxPlayers()) {
            throw new RuntimeException("already max players in room");
        }
        players.add(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public int getMaxPlayers() {
        if (maxPlayers != -1) {
            return maxPlayers;
        }
        try {
            return gameType.getConstructor().newInstance().getMaxPlayers();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("QueueUnit:\n{\n\ttype : %s\n\towner : %s \n\tplayers : %s\n}", this.gameType.getName(), this.owner, this.players);
    }

}
