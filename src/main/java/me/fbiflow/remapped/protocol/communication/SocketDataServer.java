package me.fbiflow.remapped.protocol.communication;

import me.fbiflow.remapped.protocol.PacketHolder;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.util.LoggerUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketDataServer implements PacketHolder {

    private final LoggerUtil logger = new LoggerUtil(" | [Server] -> ");

    private final ServerSocket server;

    private final List<Socket> clients = new ArrayList<>();
    private final List<Socket> lobbies = new ArrayList<>();
    private final List<Socket> sessions = new ArrayList<>();

    private final Map<Socket, List<Packet>> receivedPackets = Collections.synchronizedMap(new HashMap<>());

    public SocketDataServer(int port) {
        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Thread connectionListener = new Thread(() -> {
            try {
                while (true) {
                    Socket client = server.accept();
                    clients.add(client);
                    startPacketListener(client);
                    logger.log("Connected client: " + client);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        connectionListener.start();
    }

    public Map<Socket, List<Packet>> getReceivedPackets() {
        return this.receivedPackets;
    }

    public void sendPacket(Socket socket, Packet packet) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startPacketListener(Socket client) {
        logger.log("started client listener. Socket state is: " + client.isConnected());
        Thread packetListener = new Thread(() -> {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                while (true) {
                    Packet packet = (Packet) inputStream.readObject();
                    List<Packet> packets = receivedPackets.get(client);
                    if (packets == null) {
                        packets = new ArrayList<>();
                    }
                    packets.add(packet);
                    receivedPackets.put(client, packets);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        packetListener.start();
    }
}