package me.fbiflow.gameengine.core.model.game;

import java.util.Map;

public abstract class AbstractGame {

    public abstract int getMaxPlayers();

    public abstract int getRequiredPlayers();

    public abstract Map<String, Integer> getMaxPartyPlayers();

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onTick();

    public abstract void onEnd();

    public abstract void onRemove();

}