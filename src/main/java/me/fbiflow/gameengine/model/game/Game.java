package me.fbiflow.gameengine.model.game;

public abstract class Game {

    private static int maxPlayers;

    public static int getMaxPlayers() {
        if (maxPlayers == -1) {
            throw new IllegalStateException("Not initialized max players value of game");
        }
        return maxPlayers;
    }

    public static void setMaxPlayers(int count) {
        maxPlayers = count;
    }

    public abstract void init();

    public abstract void onTick();

    public abstract void start();

    public abstract void end();

}