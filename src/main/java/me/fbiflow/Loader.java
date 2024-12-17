package me.fbiflow;

import me.fbiflow.gameengine.core.controller.lobby.LobbyController;
import me.fbiflow.gameengine.core.controller.session.SessionController;
import me.fbiflow.gameengine.core.controller.proxy.ProxyController;
import me.fbiflow.gameengine.core.controller.session.SessionHolder;
import me.fbiflow.gameengine.core.model.game.games.Pillars;
import me.fbiflow.gameengine.core.model.wrapper.ServerContainer;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerJoinEvent;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.CleanPacket;
import me.fbiflow.test.PlayerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Loader {

    private final ProxyController proxyController
            = new ProxyController(32544, List.of(Pillars.class));

    private final LobbyController lobbyController
            = new LobbyController("127.0.0.1", 32544);

    private final SessionController sessionController
            = new SessionController("127.0.0.1", 32544,
            List.of(
                    new SessionHolder(List.of(Pillars.class)),
                    new SessionHolder(List.of(Pillars.class)),
                    new SessionHolder(List.of(Pillars.class)),
                    new SessionHolder(List.of(Pillars.class))
                    )
    );

    public static void main(String[] args) throws InterruptedException, IOException {
        Loader loader = new Loader();

        var serverContainer = new ServerContainer(ServerContainer.ContainerType.BUKKIT);

        var proxyController = loader.proxyController;
        var lobbyController = loader.lobbyController;
        var sessionController = loader.sessionController;

        proxyController.start();
        lobbyController.start();
        sessionController.start();

        lobbyController.getEventProducer().produce(new PlayerJoinEvent(PlayerMock.getPlayer("FBIFlow")));

        var cleanPacket = new CleanPacket();
        var packet = Packet.of(cleanPacket);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream tempOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            tempOutputStream.writeObject(packet);
            tempOutputStream.flush();

            System.err.println("packet size is: " + byteArrayOutputStream.size());
        }

        lobbyController.getConnection().sendPacket(packet);

    }

    private void onDisable() {
        proxyController.stop();
        lobbyController.stop();
        sessionController.stop();
    }

/*    public static void main() {
        var proxyController = new ProxyController(new SocketDataServer(34646));
        proxyController.start();
        var lobbyController = new LobbyController(new SocketDataClient("localhost", 34646));
        List<SessionHolder> sessionHolders = List.of(
                new SessionHolder(List.of(Pillars.class))
        );
        var sessionController = new SessionController(new SocketDataClient("localhost", 34646), sessionHolders);

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
                                (Class<? extends AbstractGame>) Class.forName("me.fbiflow.gameengine.core.model.game.games." + args[2]))));
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
    }*/
}