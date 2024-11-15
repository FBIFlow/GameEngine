package me.fbiflow.remapped.model.queue;

import com.google.common.base.MoreObjects;
import me.fbiflow.remapped.model.game.Game;
import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueueUnit {

    private final UUID uuid;
    private final List<Player> members;

    private Class<? extends Game> gameType;
    private Player owner;
    private int maxPlayers = 48;
    private boolean registered;

    {
        this.uuid = UUID.randomUUID();
        this.members = new ArrayList<>();
    }

    protected static QueueUnit newInstance() {
        return new QueueUnit();
    }

    private QueueUnit() {

    }

    protected UUID getUuid() {
        return this.uuid;
    }

    protected boolean isEmpty() {
        return this.members.isEmpty();
    }

    protected boolean isFull() {
        return members.size() >= maxPlayers;
    }

    protected List<Player> getMembers() {
        return this.members;
    }

    protected void addMember(Player player) {
        members.add(player);
    }

    protected void removeMember(Player player) {
        members.remove(player);
    }

    protected Class<? extends Game> getGameType() {
        return this.gameType;
    }

    protected void setGameType(Class<? extends Game> gameType) {
        this.gameType = gameType;
        try {
            Game game = gameType.getConstructor().newInstance();
            this.maxPlayers = game.getMaxPlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isOwner(Player player) {
        return this.owner.equals(player);
    }

    protected Player getOwner() {
        return this.owner;
    }

    protected void setOwner(Player owner) {
        if (!members.contains(owner)) {
            throw new RuntimeException("Player should be member of QueueUnit to be owner");
        }
        this.owner = owner;
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
        return MoreObjects.toStringHelper(this)
                .add("uuid", uuid)
                .add("members", members)
                .add("gameType", gameType)
                .add("owner", owner)
                .add("maxPlayers", maxPlayers)
                .toString();
    }
}
