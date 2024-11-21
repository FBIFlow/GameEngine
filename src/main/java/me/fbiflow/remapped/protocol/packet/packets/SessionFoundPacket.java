package me.fbiflow.remapped.protocol.packet.packets;

import me.fbiflow.remapped.protocol.packet.AbstractPacket;

import java.util.UUID;

//в SessionHolder есть свободная сессия, готов принять игроков
public class SessionFoundPacket extends AbstractPacket {

    private final UUID packetId;

    public SessionFoundPacket(UUID packetId) {
        this.packetId = packetId;
    }

    public UUID getPacketId() {
        return this.packetId;
    }

    public UUID getConnectionKey() {
        return this.packetId;
    }
}