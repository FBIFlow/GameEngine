package me.fbiflow.gameengine.core.model;

import me.fbiflow.gameengine.core.model.game.AbstractGame;

public class SessionExecutor {

    private final AbstractGame game;

    public SessionExecutor(AbstractGame game) {
        this.game = game;
    }

    public void start() {
        game.onInit();
    }

}