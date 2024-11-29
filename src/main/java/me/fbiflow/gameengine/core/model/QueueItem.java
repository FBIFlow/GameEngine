package me.fbiflow.gameengine.core.model;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.game.GameManager;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueueItem {

    private final UUID uuid;
    private final List<Player> members;
    private Class<? extends AbstractGame> gameType;
    private int maxPlayers = 48;

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

    public boolean isValid() {
        return members.size() >= GameManager.getRequiredPlayers(gameType);
    }

    public boolean isFull() {
        return members.size() == maxPlayers;
    }

    protected List<Player> getMembers() {
        return new ArrayList<>(this.members);
    }

    public Class<? extends AbstractGame> getGameType() {
        return this.gameType;
    }

    protected void addMember(Player player) {
        this.members.add(player);
    }

    protected void removeMember(Player player) {
        this.members.remove(player);
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

    @Override
    public String toString() {
        return "QueueItem{" +
                "\nuuid=" + uuid +
                ", \nmembers=" + members +
                ", \ngameType=" + gameType +
                ", \nmaxPlayers=" + maxPlayers +
                "\n}";
    }
}