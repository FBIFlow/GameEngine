package me.fbiflow.remapped.protocol.packet.packets.session;

import me.fbiflow.remapped.protocol.packet.AbstractPacket;

import java.util.UUID;

//sends when SessionHolder could`nt to find free session
public class SessionNotFoundPacket extends AbstractPacket {

    private final UUID packetId;

    public SessionNotFoundPacket(UUID packetId) {
        this.packetId = packetId;
    }

    public UUID getPacketId() {
        return this.packetId;
    }
}