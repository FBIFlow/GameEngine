package me.fbiflow.gameengine.model.wrapper.minestom;

import me.fbiflow.gameengine.model.wrapper.Player;

public class MinestomPlayer implements Player {

    private net.minestom.server.entity.Player player;

    public MinestomPlayer(net.minestom.server.entity.Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getUsername();
    }
}
