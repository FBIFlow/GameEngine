package me.fbiflow.gameengine.protocol.packet.packets.client;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

import java.util.UUID;

public class PlayerRemoveControllerPacket extends AbstractPacket {

    private final UUID controllerUUID;
    private final Player player;

    public PlayerRemoveControllerPacket(UUID controllerUUID, Player player) {
        this.controllerUUID = controllerUUID;
        this.player = player;
    }

    public UUID getControllerUUID() {
        return this.controllerUUID;
    }

    public Player getPlayer() {
        return this.player;
    }
}
