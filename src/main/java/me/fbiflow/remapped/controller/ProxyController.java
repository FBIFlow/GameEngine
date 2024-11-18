package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.common.PartyManager;
import me.fbiflow.remapped.model.common.QueueManager;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.DataSender;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.PlayerQueueJoinRequestPacket;
import me.fbiflow.remapped.protocol.packet.packets.StringPacket;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyController {

    private final DataSender dataSender;
    private final DataReceiver dataReceiver;

    PartyManager partyManager = new PartyManager();
    QueueManager queueManager = new QueueManager(partyManager);

    public ProxyController(DataSender dataSender, DataReceiver dataReceiver) {
        this.dataSender = dataSender;
        this.dataReceiver = dataReceiver;
    }

    private void handlePacket(Packet packet) {
        //dataSender.addReceiver();
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
        dataSender.send(source.sender(),
                new Packet(
                        dataSender.uuid,
                        source.sender(),
                        new StringPacket("ADDED " + packet.getWhoJoins().getName() + " to queue : \n" + queueItem).toByteArray(),
                        StringPacket.class
                ));
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                Packet received = dataReceiver.poll();
                if (received != null) {
                    System.out.println("RECEIVED PACKET");
                    handlePacket(received);
                }
            }
        });
        thread.start();
    }
}