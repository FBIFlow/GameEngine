package me.fbiflow.gameengine.core.model.wrapper.internal.event.events;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.AbstractEvent;

public class PlayerJoinEvent extends AbstractEvent {

    private final Player player;

    public PlayerJoinEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}
