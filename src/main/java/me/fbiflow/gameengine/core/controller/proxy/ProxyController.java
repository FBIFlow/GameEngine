package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.protocol.communication.SocketDataServer;
import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientRegisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.ClientUnregisterPacket;
import me.fbiflow.gameengine.protocol.packet.packets.server.ProxyStopPacket;
import me.fbiflow.gameengine.util.LoggerUtil;
import org.bukkit.event.EventHandler;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProxyController implements PacketListener {

    private final SocketDataServer server;
    private final LoggerUtil logger = new LoggerUtil("| [ProxyController] ->");
    private QueueController queueController;
    private PartyController partyController;

    private final List<Socket> lobbyControllers = new ArrayList<>();
    private final List<Socket> sessionControllers = new ArrayList<>();

    public ProxyController(SocketDataServer server) {
        this.server = server;
        CallbackService.getInstance().registerListener(this.server.getPacketProducer(), this);
    }

    protected SocketDataServer getServer() {
        return this.server;
    }

    public void start() {
        queueController = new QueueController(this);
        partyController = new PartyController(this);
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
                if (sessionControllers.contains(sender)) {
                    throw new IllegalStateException("received ClientUnregisterPacket, but client not registered as session");
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