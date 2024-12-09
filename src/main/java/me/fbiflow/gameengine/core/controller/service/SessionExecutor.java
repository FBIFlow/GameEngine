package me.fbiflow.gameengine.core.controller.service;

import me.fbiflow.gameengine.core.model.game.AbstractGame;

import java.lang.reflect.InvocationTargetException;

public class SessionExecutor {

    private final AbstractGame game;

    public SessionExecutor(Class<? extends AbstractGame> gameType) {
        AbstractGame game;
        try {
            game = gameType.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.game = game;
    }

    public void start() {
        game.onInit();
    }

}