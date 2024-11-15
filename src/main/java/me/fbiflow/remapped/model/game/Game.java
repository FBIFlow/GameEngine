package me.fbiflow.remapped.model.game;

import java.util.Map;

public abstract class Game {

    public abstract int getMaxPlayers();

    public abstract Map<String, Integer> getMaxPartyPlayers();

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onTick();

    public abstract void onEnd();

}