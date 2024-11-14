package me.fbiflow.remapped.model.wrapper.bukkit;

import me.fbiflow.remapped.model.wrapper.internal.Player;

public class BukkitPlayer implements Player {

    private final org.bukkit.entity.Player player;

    public BukkitPlayer(org.bukkit.entity.Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }
}