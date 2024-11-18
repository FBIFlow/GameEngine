package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.common.PartyManager;
import me.fbiflow.remapped.model.common.QueueManager;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.DataSender;
import me.fbiflow.remapped.protocol.impl.socket.SocketDataServer;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.PlayerQueueJoinRequestPacket;
import me.fbiflow.remapped.protocol.packet.packets.StringPacket;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyController {

    PartyManager partyManager;
    QueueManager queueManager;

    SocketDataServer server;

    public ProxyController(SocketDataServer server) {
        partyManager = new PartyManager();
        queueManager = new QueueManager(partyManager);

        this.server = server;
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
        System.out.println("added player to queue");
        System.out.println(queueItem);
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while(true) {
                server.
            }
        });
        thread.start();
    }
}