package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.core.model.PartyManager;
import me.fbiflow.gameengine.protocol.handle.CallbackService;
import me.fbiflow.gameengine.protocol.handle.PacketHandler;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataServer;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.party.*;

import java.net.Socket;

public class PartyController implements PacketListener {

    private final PartyManager partyManager;

    private final ProxyController proxyController;
    private final SocketDataServer server;

    public PartyController(ProxyController proxyController) {
        this.partyManager = new PartyManager();
        this.proxyController = proxyController;
        this.server = proxyController.getServer();
        CallbackService.getInstance().registerListener(this.server.getPacketProducer(), this);
    }

    @PacketHandler
    private void onPartyCreate(PartyCreatePacket packet, Packet source, Socket sender) {
        partyManager.createParty(packet.getOwner());
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
}