package me.fbiflow.gameengine.protocol.communication;

import me.fbiflow.gameengine.protocol.handle.PacketProducer;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.util.LoggerUtil;
import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.String.format;

public class SocketDataClient {

    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private PacketProducer packetProducer;

    private LoggerUtil logger = new LoggerUtil("| [SocketDataClient] ->");

    public SocketDataClient(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            startPacketListener();
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

    public PacketProducer getPacketProducer() {
        if (packetProducer == null) {
            packetProducer = PacketProducer.of(this);
        }
        return packetProducer;
    }

    public void sendPacket(Packet packet) {
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

    private void startPacketListener() {
        Thread packetListener = new Thread(() -> {
            this.packetProducer = getPacketProducer();
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