package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.model.common.PartyManager;
import me.fbiflow.remapped.model.common.QueueManager;
import me.fbiflow.remapped.model.party.Party;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.protocol.communication.SocketDataServer;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.client.*;
import me.fbiflow.remapped.util.LoggerUtil;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class ProxyController {

    LoggerUtil logger = new LoggerUtil(" | [ProxyController] -> ");

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
            logger.log(format("Handled packet: %s with handler: %s", source.packetClass().getSimpleName(), method.getParameters()[0].getType().getSimpleName()));
            method.invoke(this, abstractPacket, source, sender);
        } catch (IOException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.log(format("Could not to find handler for packet type: %s", source.packetClass().getName()));
        }
    }

    private void handlePacket(PartyCreatePacket packet, Packet source, Socket sender) {
        Party party = partyManager.createParty(packet.getOwner());
    }

    private void handlePacket(PartyInviteAcceptPacket packet, Packet source, Socket sender) {
        partyManager.acceptInvite(packet.getSender(), packet.getInvited());
    }

    private void handlePacket(PartyInviteAddPacket packet, Packet source, Socket sender) {
        partyManager.addInvite(packet.getSender(), packet.getInvited());
    }

    private void handlePacket(PartyInviteRemovePacket packet, Packet source, Socket sender) {
        partyManager.removeInvite(packet.getSender(), packet.getInvited());
    }

    private void handlePacket(PartyLeavePacket packet, Packet source, Socket sender) {
        partyManager.leaveParty(packet.getWhoLeaves());
    }

    private void handlePacket(PlayerQueueJoinRequestPacket packet, Packet source, Socket sender) {
        QueueItem queueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
    }

    private void handlePacket(PlayerQueueLeaveRequestPacket packet, Packet source, Socket sender) {
        queueManager.leaveQueue(packet.getWhoLeaves());
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