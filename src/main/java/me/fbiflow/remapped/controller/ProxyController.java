package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.common.PartyManager;
import me.fbiflow.remapped.model.common.QueueManager;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.protocol.communication.SocketDataServer;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.client.PlayerQueueJoinRequestPacket;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ProxyController {

    private final PartyManager partyManager;
    private final QueueManager queueManager;

    private final SocketDataServer server;

    public ProxyController(SocketDataServer server) {
        partyManager = new PartyManager();
        queueManager = new QueueManager(partyManager);

        this.server = server;

        server.start();
        this.start();
    }

    private void handlePacket(Packet source, Socket sender) {
        try {
            AbstractPacket abstractPacket = (AbstractPacket) SerializeUtil.deserialize(source.data());
            Method method = this.getClass().getDeclaredMethod("handlePacket", abstractPacket.getClass(), Packet.class, Socket.class);
            method.invoke(this, abstractPacket, source, sender);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePacket(PlayerQueueJoinRequestPacket packet, Packet source, Socket sender) {
        QueueItem queueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
    }

    private void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                Map<Socket, List<Packet>> packets = server.getReceivedPackets();
                for (Socket sender : packets.keySet()) {
                    List<Packet> packetlist = packets.get(sender);
                    if (packetlist.isEmpty()) {
                        continue;
                    }
                    handlePacket(packetlist.removeFirst(), sender);
                }
            }
        });
        thread.start();
    }
}