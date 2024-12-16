package me.fbiflow.gameengine.core.controller.session;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.protocol.enums.SessionHolderState;

import java.util.List;
import java.util.UUID;

public class SessionHolder {

    private final UUID id;
    private final List<Class<? extends AbstractGame>> allowedGameTypes;
    private SessionExecutor sessionExecutor;
    private SessionHolderState state;

    public SessionHolder(List<Class<? extends AbstractGame>> allowedGameTypes) {
        this.id = UUID.randomUUID();
        this.allowedGameTypes = allowedGameTypes;
        this.state = SessionHolderState.READY;
    }

    protected UUID getId() {
        return this.id;
    }

    protected List<Class<? extends AbstractGame>> getAllowedGameTypes() {
        return this.allowedGameTypes;
    }

    protected boolean isBusy() {
        return state == SessionHolderState.RESERVED || state == SessionHolderState.PROCESSING;
    }

    protected void reserve() {
        this.state = SessionHolderState.RESERVED;
    }

}