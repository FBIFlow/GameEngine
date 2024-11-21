package me.fbiflow.remapped.core.model;

import me.fbiflow.remapped.core.model.game.AbstractGame;
import me.fbiflow.remapped.core.model.wrapper.internal.World;
import me.fbiflow.remapped.core.model.wrapper.internal.WorldManager;

import java.util.List;

public class SessionUnit {

    private WorldManager worldManager;
    private World sessionWorld;
    private List<Class<? extends AbstractGame>> gameTypes;

    private boolean locked = false;

    private SessionUnit(Class<? extends World> worldType, String worldId, List<Class<? extends AbstractGame>> gameTypes) {
        worldManager = new WorldManager(worldType);
        this.sessionWorld = worldManager.createWorld(worldId);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}