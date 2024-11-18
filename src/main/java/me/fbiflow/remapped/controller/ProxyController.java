package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.common.PartyManager;
import me.fbiflow.remapped.model.common.QueueManager;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.protocol.impl.socket.SocketDataServer;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.PlayerQueueJoinRequestPacket;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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

    private void handlePacket(Packet packet) {
        try {
            AbstractPacket abstractPacket = (AbstractPacket) SerializeUtil.deserialize(packet.data());
            Method method = this.getClass().getDeclaredMethod("handlePacket", abstractPacket.getClass(), Packet.class);
            method.invoke(this, abstractPacket, packet);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePacket(PlayerQueueJoinRequestPacket packet, Packet source) {
        QueueItem queueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
        System.out.println("[Proxy]: Added player to queue " + queueItem);
    }

    private void start() {
        Thread thread = new Thread(() -> {
            while(true) {
                var packets = server.getReceivedPackets();
                if (packets.isEmpty()) {
                    continue;
                }
                handlePacket(packets.removeFirst());
            }
        });
        thread.start();
    }
}