package me.fbiflow.gameengine.core.model;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private final List<Player> members;
    private Player owner;
    private List<String> permissionLevel;

    private Party() {
        this.members = new ArrayList<>();
    }

    protected Player getOwner() {
        return this.owner;
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
    }

    protected List<Player> getMembers() {
        return this.members;
    }

    protected List<String> getPermissionLevel() {
        return new ArrayList<>(this.permissionLevel);
    }

    protected void setPermissionLevel(List<String> permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

}