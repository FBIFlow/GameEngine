package me.fbiflow.remapped.model;

import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private Player owner;
    private final List<Player> members;
    private List<String> permissionLevel;

    protected static Party newInstance() {
        return new Party();
    }

    private Party() {
        this.members = new ArrayList<>();
    }

    public Player getOwner() {
        return this.owner;
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
    }

    protected List<Player> getMembers() {
        return this.members;
    }

    public List<String> getPermissionLevel() {
        return this.permissionLevel;
    }

    public void setPermissionLevel(List<String> permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public List<Player> getMembersCopy() {
        return new ArrayList<>(this.members);
    }
}