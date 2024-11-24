package me.fbiflow.gameengine.protocol.packet.packets.session;

import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

import java.util.UUID;

//в SessionHolder есть свободная сессия, готов принять игроков
public class SessionFoundPacket extends AbstractPacket {

    private final UUID packetId;
    private final UUID sessionId;

    public SessionFoundPacket(UUID packetId, UUID sessionId) {
        this.packetId = packetId;
        this.sessionId = sessionId;
    }

    public UUID getPacketId() {
        return this.packetId;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }
}