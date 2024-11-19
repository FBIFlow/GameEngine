package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.communication.SocketDataClient;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.client.PlayerQueueJoinRequestPacket;

public class LobbyController {

    private final SocketDataClient client;

    public LobbyController(SocketDataClient client) {
        this.client = client;
    }

    public void sendPlayerJoin(Player whoJoins, Class<? extends AbstractGame> gameType) {
        PlayerQueueJoinRequestPacket playerQueueJoinRequestPacket = new PlayerQueueJoinRequestPacket(whoJoins, gameType);
        Packet packet = new Packet(
                playerQueueJoinRequestPacket.toByteArray(),
                playerQueueJoinRequestPacket.getClass()
        );
        client.sendPacket(packet);
    }
}