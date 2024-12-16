package me.fbiflow.gameengine.core.model.wrapper.bukkit;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.core.model.wrapper.internal.PlayerManager;
import org.bukkit.Bukkit;

public class BukkitPlayerManager implements PlayerManager {

    private static BukkitPlayerManager instance;

    public static BukkitPlayerManager getInstance() {
        if (instance == null) {
            instance = new BukkitPlayerManager();
        }
        return instance;
    }

    @Override
    public Player getPlayer(String name) {
        return new BukkitPlayer(Bukkit.getPlayer(name));
    }

    @Override
    public Player getPlayer(Object serverPlayer) {
        if (serverPlayer instanceof org.bukkit.entity.Player) {
            return new BukkitPlayer((org.bukkit.entity.Player) serverPlayer);
        }
        throw new IllegalArgumentException("argument may be bukkit player");
    }
}