package me.fbiflow.remapped.protocol.impl.socket;

import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketDataClient {

    private final Socket serverConnection;
    private final SocketAddress socketAddress;

    public SocketDataClient(String host, int port) {
        this.socketAddress = new InetSocketAddress(host, port);
        this.serverConnection = new Socket();
    }

    public void start() {
        try {
            serverConnection.connect(socketAddress);
            if (!serverConnection.isConnected()) {
                throw new RuntimeException("could`nt to find a host");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {
            try {
                InputStream inputStream = serverConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendData(Packet packet) {
        try {
            OutputStream outputStream = serverConnection.getOutputStream();
            outputStream.write(SerializeUtil.serialize(packet));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleData(Packet receivedPacket) {

    }
}
