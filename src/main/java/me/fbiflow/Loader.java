package me.fbiflow;

import me.fbiflow.gameengine.core.controller.LobbyController;
import me.fbiflow.gameengine.core.model.SessionHolder;
import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.game.games.Pillars;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.protocol.communication.SocketDataClient;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.party.*;
import me.fbiflow.gameengine.protocol.packet.packets.queue.PlayerQueueJoinRequestPacket;
import me.fbiflow.gameengine.protocol.packet.packets.queue.PlayerQueueLeaveRequestPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.util.List;
import java.util.Scanner;

import static me.fbiflow.test.PlayerMock.getPlayer;

public class Loader {

    public static void main() {
        //var proxyController = new ProxyController(new SocketDataServer(34646));
        var lobbyController = new LobbyController(new SocketDataClient("localhost", 34646));
        List<SessionHolder> sessionHolders = List.of();
//        var sessionHolderController = new SessionHolderController(new SocketDataClient("localhost", 34646), ));

        LoggerUtil logger = new LoggerUtil(" | [Loader] -> ");

        Class<? extends AbstractGame> gameType = Pillars.class;
        Scanner scanner = new Scanner(System.in);

        for (String input; !(input = scanner.nextLine()).equals("exit"); ) {
            String[] args = input.split(" ");
            try {
                switch (args[0]) {
                    case "party-create" -> {
                        Player player = getPlayer(args[1]);
                        lobbyController.getConnection().sendPacket(Packet.of(new PartyCreatePacket(player)));
                    }
                    case "party-leave" -> {
                        Player player = getPlayer(args[1]);
                        lobbyController.getConnection().sendPacket(Packet.of(new PartyLeavePacket(player)));
                    }
                    case "party-invite-add" -> {
                        Player sender = getPlayer(args[1]);
                        Player invited = getPlayer(args[2]);
                        lobbyController.getConnection().sendPacket(Packet.of(new PartyInviteAddPacket(sender, invited)));
                    }
                    case "party-invite-remove" -> {
                        Player sender = getPlayer(args[1]);
                        Player invited = getPlayer(args[2]);
                        lobbyController.getConnection().sendPacket(Packet.of(new PartyInviteRemovePacket(sender, invited)));
                    }
                    case "party-invite-accept" -> {
                        Player sender = null;
                        Player invited = null;
                        if (args.length == 2) { // command player
                            invited = getPlayer(args[1]);
                        } else if (args.length == 3) { //command player player
                            sender = getPlayer(args[1]);
                            invited = getPlayer(args[2]);
                        }
                        lobbyController.getConnection().sendPacket(Packet.of(new PartyInviteAcceptPacket(sender, invited)));
                    }
                    case "queue-join" -> {
                        Player player = getPlayer(args[1]);
                        lobbyController.getConnection().sendPacket(Packet.of(new PlayerQueueJoinRequestPacket(
                                player,
                                (Class<? extends AbstractGame>) Class.forName("me.fbiflow.remapped.model.game.games." + args[2]))));
                    }
                    case "queue-leave" -> {
                        Player player = getPlayer(args[1]);
                        lobbyController.getConnection().sendPacket(Packet.of(new PlayerQueueLeaveRequestPacket(player)));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.log("Required more args");
            } catch (ClassNotFoundException e) {
                logger.log("Incorrect game type");
            }
        }

    }
}