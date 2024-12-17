package me.fbiflow.gameengine.core.model.wrapper.bukkit.manager;

import me.fbiflow.gameengine.core.model.wrapper.bukkit.BukkitPlayer;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.core.model.wrapper.internal.manager.PlayerManager;
import org.bukkit.Bukkit;

public class BukkitPlayerManager implements PlayerManager {

    @Override
    public Player getPlayer(String name) {
        return new BukkitPlayer(Bukkit.getPlayer(name));
    }

    @Override
    public Player getPlayer(Object serverPlayerImpl) {
        if (!(serverPlayerImpl instanceof org.bukkit.entity.Player)) {
            throw new IllegalArgumentException("may be org.bukkit.Player");
        }
        return new BukkitPlayer(((org.bukkit.entity.Player) serverPlayerImpl));
    }
}