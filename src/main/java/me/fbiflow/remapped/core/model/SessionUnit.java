package me.fbiflow.remapped.core.model;

import me.fbiflow.remapped.core.model.game.AbstractGame;
import me.fbiflow.remapped.core.model.wrapper.internal.World;
import me.fbiflow.remapped.core.model.wrapper.internal.WorldManager;

import java.util.List;
import java.util.UUID;

public class SessionUnit {

    private WorldManager worldManager;
    private World sessionWorld;
    private List<Class<? extends AbstractGame>> gameTypes;

    private UUID reservedBy = null;

    private SessionUnit(Class<? extends World> worldType, String worldId, List<Class<? extends AbstractGame>> gameTypes) {
        worldManager = new WorldManager(worldType);
        this.sessionWorld = worldManager.createWorld(worldId);
    }

    public void setup() {
        //create world
        //init
    }

    public boolean isReserved() {
        return this.reservedBy != null;
    }

    public void reserve(UUID packetId) {
        this.reservedBy = packetId;
    }
}