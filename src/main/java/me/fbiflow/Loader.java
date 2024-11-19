package me.fbiflow;

import me.fbiflow.remapped.controller.LobbyController;
import me.fbiflow.remapped.controller.ProxyController;
import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.game.games.Pillars;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.communication.SocketDataClient;
import me.fbiflow.remapped.protocol.communication.SocketDataServer;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.client.PlayerQueueJoinRequestPacket;
import me.fbiflow.test.PlayerMock;

import java.util.Scanner;

import static me.fbiflow.test.PlayerMock.getPlayer;

public class Loader {

    public static void main() {
        var proxyController = new ProxyController(new SocketDataServer(34646));
        var lobbyController = new LobbyController(new SocketDataClient("localhost", 34646));

        Class<? extends AbstractGame> gameType = Pillars.class;
        for (String name; !(name = new Scanner(System.in).nextLine()).isEmpty();) {
            lobbyController.sendPacket(Packet.of(new PlayerQueueJoinRequestPacket(getPlayer(name), gameType)));
        }
    }
}