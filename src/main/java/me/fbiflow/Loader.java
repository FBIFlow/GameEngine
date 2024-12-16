package me.fbiflow;

import me.fbiflow.gameengine.core.controller.lobby.LobbyController;
import me.fbiflow.gameengine.core.controller.session.SessionController;
import me.fbiflow.gameengine.core.controller.proxy.ProxyController;
import me.fbiflow.gameengine.core.controller.session.SessionHolder;
import me.fbiflow.gameengine.core.model.game.games.Pillars;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerJoinEvent;
import me.fbiflow.gameengine.protocol.communication.Client;
import me.fbiflow.gameengine.protocol.communication.Server;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.DataPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.queue.PlayerQueueJoinRequestPacket;

import java.util.List;

import static me.fbiflow.test.PlayerMock.getPlayer;

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

    public static void main(String[] args) throws InterruptedException {
        Loader loader = new Loader();

        var proxyController = loader.proxyController;
        var lobbyController = loader.lobbyController;
        var sessionController = loader.sessionController;

        proxyController.start();
        lobbyController.start();
        sessionController.start();

        Thread.sleep(3000);

        var packet = new PlayerQueueJoinRequestPacket(getPlayer("FBIFlow"), Pillars.class);
        System.out.println("time before serializing packet: " + System.currentTimeMillis());
        System.out.println("size of packet is: " + packet.toByteArray().length);
        System.out.println("time after serializing packet: " + System.currentTimeMillis());
        lobbyController.getConnection().sendPacket(Packet.of(packet));

        //lobbyController.getEventProducer().produce(new PlayerJoinEvent(getPlayer("FBIFlow")));
        /*lobbyController.getConnection().sendPacket(
                Packet.of(new DataPacket())
        );
        System.out.println("time after sending 1 packet: " + System.currentTimeMillis());

        lobbyController.getConnection().sendPacket(
                Packet.of(new DataPacket())
        );
        System.out.println("time after sending 2 packet: " + System.currentTimeMillis());*/
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