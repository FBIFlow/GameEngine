package me.fbiflow.gameengine.core.model;

import me.fbiflow.gameengine.core.controller.service.SessionExecutor;
import me.fbiflow.gameengine.core.model.game.AbstractGame;

import java.util.List;
import java.util.UUID;

public class SessionHolder {

    private final UUID id;
    private final List<Class<? extends AbstractGame>> allowedGameTypes;

    private SessionExecutor sessionExecutor;

    public SessionHolder(List<Class<? extends AbstractGame>> allowedGameTypes) {
        id = UUID.randomUUID();
        this.allowedGameTypes = allowedGameTypes;
    }

    public UUID getId() {
        return this.id;
    }

    public List<Class<? extends AbstractGame>> getAllowedGameTypes() {
        return this.allowedGameTypes;
    }
}