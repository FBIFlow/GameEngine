package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.DataSender;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.PlayerQueueJoinRequestPacket;

public class LobbyController {

    private final DataSender dataSender;
    private final DataReceiver dataReceiver;

    public LobbyController(DataSender dataSender, DataReceiver dataReceiver) {
        this.dataSender = dataSender;
        this.dataReceiver = dataReceiver;
    }

    public void sendPlayerJoin(Player whoJoins, Class<? extends AbstractGame> gameType) {
        PlayerQueueJoinRequestPacket playerQueueJoinRequestPacket = new PlayerQueueJoinRequestPacket(whoJoins, gameType);
        Packet packet = new Packet(dataSender.uuid, null,
                playerQueueJoinRequestPacket.toByteArray(),
                playerQueueJoinRequestPacket.getClass()
        );
        sendPacket(packet);
    }

    private void sendPacket(Packet packet) {
        if (!dataSender.hasDefaultReceiver()) {
            throw new RuntimeException("Lobby sender has`nt specified default receiver");
        } else {
            System.out.println("sendPacket: good 1");
        }
        dataSender.send(null, packet);
    }
}