package me.fbiflow;

import me.fbiflow.remapped.controller.LobbyController;
import me.fbiflow.remapped.controller.ProxyController;
import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.game.games.Pillars;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.impl.socket.SocketDataClient;
import me.fbiflow.remapped.protocol.impl.socket.SocketDataServer;
import me.fbiflow.test.PlayerMock;

import java.io.IOException;
import java.util.Scanner;

public class Loader {

    public static void main(String[] arguments) throws IOException {
        var proxyController = new ProxyController(new SocketDataServer(53525));
        var lobbyController = new LobbyController(new SocketDataClient("localhost", 53525));

        Player player = new PlayerMock("FBIFlow");
        Class<? extends AbstractGame> gameType = Pillars.class;
        for (String name; !(name = new Scanner(System.in).nextLine()).isEmpty();) {
            lobbyController.sendPlayerJoin(new PlayerMock(name), gameType);
        }
    }
}