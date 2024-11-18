package me.fbiflow.remapped.protocol.impl.socket;

import me.fbiflow.remapped.protocol.common.ByteBufferOutputStream;
import me.fbiflow.remapped.protocol.packet.Packet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketDataClient {

    private final SocketChannel socketChannel;
    private ObjectOutputStream outputStream;

    public SocketDataClient(String host, int port) {
        try {
            this.socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            this.socketChannel.configureBlocking(false); // Неблокирующий режим
            this.outputStream = new ObjectOutputStream(new ByteBufferOutputStream(socketChannel));
            System.out.println("[Client]: Connected to server: " + host + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPacket(Packet packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
            System.out.println("[Client]: Sent packet: " + packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Packet packet = receivePacket();
                    if (packet != null) {
                        System.out.println("[Client]: Received packet: " + packet);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private Packet receivePacket() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[bytesRead];
            buffer.get(data);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                return (Packet) ois.readObject();
            }
        }
        return null;
    }
}
