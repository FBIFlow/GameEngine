package me.fbiflow.gameengine.protocol.communication;

import me.fbiflow.gameengine.protocol.handle.PacketProducer;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.util.LoggerUtil;
import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

import static java.lang.String.format;

public class Client {

    private final UUID clientId;
    private final InetSocketAddress host;

    private final PacketProducer packetProducer;

    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private LoggerUtil logger = new LoggerUtil("| [SocketDataClient] ->");

    public Client(String host, int port, PacketProducer packetProducer) {
        this.clientId = UUID.randomUUID();
        this.host = new InetSocketAddress(host, port);
        this.packetProducer = packetProducer;
    }

    public void start() {
        try {
            this.socket = new Socket(host.getHostName(), host.getPort());
            startPacketListener();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public String getAddress() {
        return socket.toString();
    }


    public synchronized void sendPacket(Packet packet) {
        System.err.println("start sending packet: " + System.currentTimeMillis());
        try {
            if (objectOutputStream == null) {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            }
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.err.println("end sending packet: " + System.currentTimeMillis());
    }

    private void startPacketListener() {
        Thread packetListener = new Thread(() -> {
            try {
                if (objectInputStream == null) {
                    objectInputStream = new ObjectInputStream(socket.getInputStream());
                }
                while (true) {
                    if (socket.isClosed() || !socket.isConnected()) {
                        break;
                    }
                    Packet packet = (Packet) objectInputStream.readObject();
                    logReceive(packet, socket);
                    packetProducer.produce(packet, socket);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        packetListener.start();
    }

    private void logReceive(Packet packet, Socket sender) throws IOException, ClassNotFoundException {
        AbstractPacket abstractPacket = (AbstractPacket) SerializeUtil.deserialize(packet.abstractPacket());
        logger.log(format("Received packet: {\n\t%s{%s}\n\t%s{%s}\n\t{%s}\n\t%s\n}",
                packet.getClass().getSimpleName(),
                packet.hashCode(),
                abstractPacket.getClass().getSimpleName(),
                abstractPacket.hashCode(),
                abstractPacket,
                sender.getRemoteSocketAddress()
        ));
    }
}