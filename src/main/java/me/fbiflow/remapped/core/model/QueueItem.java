package me.fbiflow.remapped.core.model;

import me.fbiflow.remapped.core.model.game.AbstractGame;
import me.fbiflow.remapped.core.model.wrapper.internal.Player;

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

    private QueueItem() {

    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isEmpty() {
        return this.members.isEmpty();
    }

    public boolean isFull() {
        return members.size() == maxPlayers;
    }

    public List<Player> getMembers() {
        return this.members;
    }

    public List<Player> getMembersCopy() {
        return new ArrayList<>(this.members);
    }

    public Class<? extends AbstractGame> getGameType() {
        return this.gameType;
    }

    public void setGameType(Class<? extends AbstractGame> gameType) {
        this.gameType = gameType;
        try {
            AbstractGame abstractGame = gameType.getConstructor().newInstance();
            this.maxPlayers = abstractGame.getMaxPlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "QueueItem{" +
                "\nuuid=" + uuid +
                ", \nmembers=" + members +
                ", \ngameType=" + gameType +
                ", \nmaxPlayers=" + maxPlayers +
                ", \nregistered=" + registered +
                "\n}";
    }
}