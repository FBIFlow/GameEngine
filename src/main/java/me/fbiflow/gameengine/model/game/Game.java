package me.fbiflow.gameengine.model.game;

import com.google.common.base.MoreObjects;

public abstract class Game {

    public abstract int getMaxPlayers();

    public abstract void init();

    public abstract void onTick();

    public abstract void start();

    public abstract void end();

}