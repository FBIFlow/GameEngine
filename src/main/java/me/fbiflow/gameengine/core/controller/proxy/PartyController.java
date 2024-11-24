package me.fbiflow.gameengine.core.controller.proxy;

import me.fbiflow.gameengine.core.model.PartyManager;
import me.fbiflow.gameengine.protocol.PacketHandler;
import me.fbiflow.gameengine.protocol.PacketListener;
import me.fbiflow.gameengine.protocol.communication.SocketDataServer;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.party.*;

import java.net.Socket;

public class PartyController extends PacketListener {

    private final PartyManager partyManager;

    private SocketDataServer server;

    public PartyController(SocketDataServer server) {
        this.partyManager = new PartyManager();

        this.server = server;
        startListener(this.server);
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