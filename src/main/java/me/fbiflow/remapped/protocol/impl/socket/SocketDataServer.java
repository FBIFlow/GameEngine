package me.fbiflow.remapped.protocol.impl.socket;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketDataServer {

    private final ServerSocketChannel serverSocketChannel;
    private final List<Packet> receivedPackets = Collections.synchronizedList(new ArrayList<>());

    public SocketDataServer(int port) {
        try {
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while(true) {
                try {
                    Socket connection = serverSocket.accept();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    public Packet waitForPacket() {

    }
}
