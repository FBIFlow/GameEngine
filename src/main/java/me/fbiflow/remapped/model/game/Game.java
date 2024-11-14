package me.fbiflow.remapped.model.game;

public abstract class Game {

    public abstract int getMaxPlayers();

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onTick();

    public abstract void onEnd();

}