package me.fbiflow.remapped.protocol.impl.socket;

import me.fbiflow.remapped.model.game.games.Pillars;
import me.fbiflow.remapped.protocol.DataExchanger;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.PlayerQueueJoinRequestPacket;
import me.fbiflow.remapped.protocol.packet.packets.StringPacket;
import me.fbiflow.remapped.util.SerializeUtil;
import me.fbiflow.test.PlayerMock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketDataServer extends DataExchanger {

    private final ServerSocket serverSocket;
    private final Map<UUID ,Socket> clients = Collections.synchronizedMap(new HashMap<>());

    public SocketDataServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        Thread thread = new Thread(() -> {
            while(true) {
                try {
                    Socket connection = serverSocket.accept();
                    UUID uuid = UUID.randomUUID();
                    clients.put(uuid, connection);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(SerializeUtil.serialize(uuid));
                    outputStream.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    @Override
    public void sendData(Packet packet) {
        Socket socket = clients.get(packet.receiver());
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(SerializeUtil.serialize(packet));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleData(Packet receivedPacket) {

    }
}
