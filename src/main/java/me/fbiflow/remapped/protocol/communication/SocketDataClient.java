package me.fbiflow.remapped.protocol.communication;

import me.fbiflow.remapped.protocol.PacketHolder;
import me.fbiflow.remapped.protocol.packet.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class SocketDataClient implements PacketHolder {

    private Socket socket;

    private final Map<Socket, List<Packet>> receivedPackets = Collections.synchronizedMap(new HashMap<>());

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public SocketDataClient(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            startPacketListener();
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
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
            try {
                if (objectInputStream == null) {
                    objectInputStream = new ObjectInputStream(socket.getInputStream());
                }
                while (true) {
                    if (socket.isClosed() || !socket.isConnected()) {
                        break;
                    }
                    Packet packet = (Packet) objectInputStream.readObject();
                    List<Packet> packets = receivedPackets.get(socket);
                    if (packets == null) {
                        packets = new ArrayList<>();
                    }
                    packets.add(packet);
                    receivedPackets.put(socket, packets);
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

    @Override
    public Map<Socket, List<Packet>> getReceivedPackets() {
        return this.receivedPackets;
    }
}