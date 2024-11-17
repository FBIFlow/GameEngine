package me.fbiflow.remapped.model.queue;

import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.List;
import java.util.UUID;

public record QueueItemState(UUID uuid, List<Player> members, Class<? extends AbstractGame> gameType, int maxPlayers, boolean registered) {
}