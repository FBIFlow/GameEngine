package me.fbiflow.remapped.protocol.impl.socket;

import me.fbiflow.remapped.protocol.common.ByteBufferInputStream;
import me.fbiflow.remapped.protocol.packet.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SocketDataServer {

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final List<Packet> receivedPackets = Collections.synchronizedList(new ArrayList<>());

    public SocketDataServer(int port) {
        try {
            this.serverSocketChannel = ServerSocketChannel.open();
            System.out.println("[Server]: opened channel");
            this.serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("[Server] bind address");
            this.serverSocketChannel.configureBlocking(false);

            this.selector = Selector.open();
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    selector.select();

                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();

                        if (key.isAcceptable()) {
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("[Server]: Client connected: " + clientChannel.getRemoteAddress());
                        } else if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            handleClientData(clientChannel);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void handleClientData(SocketChannel clientChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                System.out.println("[Server]: Client disconnected: " + clientChannel.getRemoteAddress());
                clientChannel.close();
            } else {
                buffer.flip();
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteBufferInputStream(buffer));
                Packet packet = (Packet) objectInputStream.readObject();
                receivedPackets.add(packet);
                System.out.println("[Server]: Received Packet: " + packet);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Packet> getReceivedPackets() {
        return this.receivedPackets;
    }
}
