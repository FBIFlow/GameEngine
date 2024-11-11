package me.fbiflow.gameengine.model.wrapper.bukkit;

public class BukkitPlayer implements me.fbiflow.gameengine.model.wrapper.Player {

    private final org.bukkit.entity.Player player;

    public BukkitPlayer(org.bukkit.entity.Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getName();
    }
}