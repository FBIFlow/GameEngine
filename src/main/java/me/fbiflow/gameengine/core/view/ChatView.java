package me.fbiflow.gameengine.core.view;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ChatView {

    public void sendMessage(Player player, Component message) {
        player.sendMessage(message);
    }

    public void broadcast(List<Player> players, Component message) {
        players.forEach(p -> p.sendMessage(message));
    }


}