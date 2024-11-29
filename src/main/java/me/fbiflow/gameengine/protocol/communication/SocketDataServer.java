package me.fbiflow.gameengine.protocol.communication;

import me.fbiflow.gameengine.protocol.handle.PacketProducer;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.util.LoggerUtil;
import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class SocketDataServer {

    private final ServerSocket server;

    private final List<Socket> clients = new ArrayList<>();
    private final List<Socket> lobbies = new ArrayList<>();
    private final List<Socket> sessions = new ArrayList<>();

    private PacketProducer packetProducer;

    private LoggerUtil logger = new LoggerUtil("| [SocketDataServer] ->");

    public SocketDataServer(int port) {
        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PacketProducer getPacketProducer() {
        if (packetProducer == null) {
            packetProducer = PacketProducer.of(this);
        }
        return packetProducer;
    }

    public void start() {
        this.packetProducer = getPacketProducer();
        Thread connectionListener = new Thread(() -> {
            try {
                while (true) {
                    Socket client = server.accept();
                    clients.add(client);
                    logger.log("Connected client: " + client);
                    startPacketListener(client);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        connectionListener.start();
    }

    private ObjectOutputStream objectOutputStream = null;

    public void sendPacket(Socket socket, Packet packet) {
        try {
            if (objectOutputStream == null) {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            }
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startPacketListener(Socket sender) {
        Thread packetListener = new Thread(() -> {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(sender.getInputStream());
                while (true) {
                    Packet packet = (Packet) objectInputStream.readObject();
                    logReceive(packet, sender);
                    packetProducer.produce(packet, sender);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        packetListener.start();
    }

    private void logReceive(Packet packet, Socket sender) throws IOException, ClassNotFoundException {
        AbstractPacket abstractPacket = (AbstractPacket) SerializeUtil.deserialize(packet.abstractPacket());
        logger.log(format("received packet: {\n\t%s{%s}\n\t%s{%s}\n\t{%s}\n\t%s\n}",
                packet.getClass().getSimpleName(),
                packet.hashCode(),
                abstractPacket.getClass().getSimpleName(),
                abstractPacket.hashCode(),
                abstractPacket,
                sender.getRemoteSocketAddress()
        ));
    }

}