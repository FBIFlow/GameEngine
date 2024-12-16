package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.game.GameManager;
import me.fbiflow.gameengine.protocol.communication.Server;
import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.PacketHandleService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.handle.PacketProducer;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.DataPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientRegisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientUnregisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.ProxyStopPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class ProxyController implements PacketListener {

    private final Server server;
    private final LoggerUtil logger = new LoggerUtil("| [ProxyController] ->");
    private ProxyQueueController proxyQueueController;
    private ProxyPartyController proxyPartyController;

    private final List<Socket> lobbyControllers = new ArrayList<>();
    private final List<Socket> sessionControllers = new ArrayList<>();

    private final PacketProducer packetProducer = PacketProducer.of(this);

    private final List<Class<? extends AbstractGame>> allowedGames;

    public ProxyController(int port, List<Class<? extends AbstractGame>> allowedGames) {
        List<String> ids = new ArrayList<>();
        allowedGames.forEach(game -> {
            String id = GameManager.getId(game);
            if (ids.contains(id)) {
                throw new RuntimeException("trying to register game, but game with same id already registered");
            }
            ids.add(id);
        });
        this.allowedGames = allowedGames;
        this.server = new Server(port, packetProducer);
        PacketHandleService.getInstance().registerListener(this.packetProducer, this);
    }

    public PacketProducer getPacketProducer() {
        return this.packetProducer;
    }

    protected Server getServer() {
        return this.server;
    }

    public void start() {
        proxyQueueController = new ProxyQueueController(this);
        proxyPartyController = new ProxyPartyController(this);
        server.start();
    }

    public void stop() {
        broadcastPacket(Packet.of(new ProxyStopPacket()));
        logger.log("Disabled");
    }

    protected void sendPacket(Socket socket, Packet packet) {
        server.sendPacket(socket, packet);
    }

    protected void broadcastPacket(Packet packet) {
        Arrays.stream(ClientType.values()).forEach(x -> broadcastPacket(packet, x));
    }

    protected void broadcastPacket(Packet packet, ClientType clientType) {
        switch (clientType) {
            case LOBBY_CONTROLLER -> lobbyControllers.forEach(lobbySocket -> sendPacket(lobbySocket, packet));
            case SESSION_CONTROLLER -> sessionControllers.forEach(lobbySocket -> sendPacket(lobbySocket, packet));
        }
    }

    @PacketHandler
    private void onDataPacket(DataPacket packet, Packet source, Socket sender) {
        System.out.println("time when packet received: " + System.currentTimeMillis());
    }

    @PacketHandler
    private void onClientRegisterReceive(ClientRegisterPacket packet, Packet source, Socket sender) {
        if (lobbyControllers.contains(sender) || sessionControllers.contains(sender)) {
            throw new IllegalStateException("received ClientRegisterPacket, but client already registered");
        }
        switch (packet.getClientType()) {
            case LOBBY_CONTROLLER -> {
                if (lobbyControllers.contains(sender)) {
                    throw new IllegalStateException("received ClientRegisterPacket, but client already registered as lobby");
                }
                lobbyControllers.add(sender);
            }
            case SESSION_CONTROLLER -> {
                packet.getAllowedGames().forEach((gameId, hashcode) -> {
                    List<String> ids = allowedGames.stream().map(GameManager::getId).toList();
                    if (!ids.contains(gameId)) {
                        throw new RuntimeException(format("game session registered with game %s, but this game is`nt contains in proxy", gameId));
                    }
                    for (Class<? extends AbstractGame> gameType : allowedGames) {
                        if (!GameManager.getId(gameType).equals(gameId)) {
                            continue;
                        }
                        if (GameManager.hashCode(gameType) != hashcode) {
                            throw new RuntimeException(format("incorrect hashcode of game %s", gameId));
                        }
                        return;
                    }
                    throw new RuntimeException("unhandled exception");
                });
                if (sessionControllers.contains(sender)) {
                    throw new IllegalStateException("received ClientRegisterPacket, but client already registered as session");
                }
                sessionControllers.add(sender);
            }
        }
    }

    @PacketHandler
    private void onClientUnregisterPacket(ClientUnregisterPacket packet, Packet source, Socket sender) {
        switch (packet.getClientType()) {
            case LOBBY_CONTROLLER -> {
                if (!lobbyControllers.contains(sender)) {
                    throw new IllegalStateException("received ClientUnregisterPacket, but client not registered as lobby");
                }
                lobbyControllers.remove(sender);
            }
            case SESSION_CONTROLLER -> {
                if (!sessionControllers.contains(sender)) {
                    throw new IllegalStateException("received ClientUnregisterPacket, but client not registered as session");
                }
                sessionControllers.remove(sender);
            }
        }
    }
}