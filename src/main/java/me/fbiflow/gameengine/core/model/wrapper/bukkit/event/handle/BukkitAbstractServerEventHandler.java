package me.fbiflow.gameengine.core.model.wrapper.bukkit.event.handle;

import me.fbiflow.gameengine.core.controller.event.EventProducer;
import me.fbiflow.gameengine.core.model.wrapper.bukkit.manager.BukkitPlayerManager;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerJoinEvent;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.handle.AbstractServerEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class BukkitAbstractServerEventHandler extends AbstractServerEventHandler implements Listener {

    private final Plugin plugin;
    private final BukkitPlayerManager playerManager;

    {
        this.playerManager = new BukkitPlayerManager();
    }

    public BukkitAbstractServerEventHandler(Plugin plugin, EventProducer eventProducer) {
        super(eventProducer);
        this.plugin = plugin;
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
         eventProducer.produce(new PlayerJoinEvent(
                playerManager.getPlayer(event.getPlayer())
        ));
    }
}
