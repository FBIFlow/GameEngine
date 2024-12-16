package me.fbiflow.gameengine.core.controller.session;

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
}