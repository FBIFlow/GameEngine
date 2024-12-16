package me.fbiflow.gameengine.core.model.wrapper.bukkit;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import net.kyori.adventure.text.Component;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public class BukkitPlayer implements Player {

    private final org.bukkit.entity.Player player;

    public BukkitPlayer(org.bukkit.entity.Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public List<String> getPermissions() {
        return player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).toList();
    }

    @Override
    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

}