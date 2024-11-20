package me.fbiflow.remapped.core.controller;

import me.fbiflow.remapped.core.model.PartyManager;
import me.fbiflow.remapped.core.model.QueueManager;
import me.fbiflow.remapped.core.model.QueueItem;
import me.fbiflow.remapped.protocol.PacketHandler;
import me.fbiflow.remapped.protocol.PacketHolder;
import me.fbiflow.remapped.protocol.PacketListener;
import me.fbiflow.remapped.protocol.communication.SocketDataServer;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.*;
import me.fbiflow.remapped.util.LoggerUtil;

import java.net.Socket;

public class ProxyController implements PacketListener {

    private final PartyManager partyManager;
    private final QueueManager queueManager;
    private final SocketDataServer server;
    LoggerUtil logger = new LoggerUtil(" | [ProxyController] -> ");

    public ProxyController(SocketDataServer server) {
        partyManager = new PartyManager();
        queueManager = new QueueManager(partyManager);

        this.server = server;

        server.start();
        startListener(server);
    }

    @PacketHandler
    private void onPartyCreate(PartyCreatePacket packet, Packet source, Socket sender) {
        server.sendPacket(sender, Packet.of(new PartyCreatePacket(packet.getOwner())));
    }

    @PacketHandler
    private void onPartyInviteAccept(PartyInviteAcceptPacket packet, Packet source, Socket sender) {
        partyManager.acceptInvite(packet.getSender(), packet.getInvited());
    }

    @PacketHandler
    private void onPartyInviteAdd(PartyInviteAddPacket packet, Packet source, Socket sender) {
        partyManager.addInvite(packet.getSender(), packet.getInvited());
    }

    @PacketHandler
    private void onPartyInviteRemove(PartyInviteRemovePacket packet, Packet source, Socket sender) {
        partyManager.removeInvite(packet.getSender(), packet.getInvited());
    }

    @PacketHandler
    private void onPartyLeave(PartyLeavePacket packet, Packet source, Socket sender) {
        partyManager.leaveParty(packet.getWhoLeaves());
    }

    @PacketHandler
    private void onPlayerQueueJoin(PlayerQueueJoinRequestPacket packet, Packet source, Socket sender) {
        QueueItem queueItem = queueManager.joinQueue(packet.getWhoJoins(), packet.getGameType());
    }

    @PacketHandler
    private void onPlayerQueueLeave(PlayerQueueLeaveRequestPacket packet, Packet source, Socket sender) {
        queueManager.leaveQueue(packet.getWhoLeaves());
    }

    @Override
    public LoggerUtil getLogger() {
        return this.logger;
    }

    @Override
    public void handlePacket(Packet source, Socket sender) {
        PacketListener.super.handlePacket(source, sender);
    }

    @Override
    public void startListener(PacketHolder packetHolder) {
        PacketListener.super.startListener(packetHolder);
    }
}