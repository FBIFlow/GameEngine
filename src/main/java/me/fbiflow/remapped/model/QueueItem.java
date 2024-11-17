package me.fbiflow.remapped.model;

import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueueItem {

    private final UUID uuid;
    private final List<Player> members;

    private Class<? extends AbstractGame> gameType;
    private int maxPlayers = 48;
    private boolean registered;

    {
        this.uuid = UUID.randomUUID();
        this.members = new ArrayList<>();
    }

    protected static QueueItem newInstance() {
        return new QueueItem();
    }

    private QueueItem() {

    }

    protected UUID getUuid() {
        return this.uuid;
    }

    protected boolean isEmpty() {
        return this.members.isEmpty();
    }

    protected boolean isFull() {
        return members.size() == maxPlayers;
    }

    protected List<Player> getMembers() {
        return this.members;
    }

    protected Class<? extends AbstractGame> getGameType() {
        return this.gameType;
    }

    protected void setGameType(Class<? extends AbstractGame> gameType) {
        this.gameType = gameType;
        try {
            AbstractGame abstractGame = gameType.getConstructor().newInstance();
            this.maxPlayers = abstractGame.getMaxPlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getMaxPlayers() {
        return this.maxPlayers;
    }

    protected boolean isRegistered() {
        return this.registered;
    }

    protected void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "QueueItem{" +
                "uuid=" + uuid +
                ", members=" + members +
                ", gameType=" + gameType +
                ", maxPlayers=" + maxPlayers +
                ", registered=" + registered +
                '}';
    }
}