package me.fbiflow.remapped.protocol.packet.packets.session;

import java.util.UUID;

//register new listener in SessionHolder, that will be callback when new session is founded
public class SessionGetCallbackRequestPacket {

    private final UUID packetId;

    public SessionGetCallbackRequestPacket(UUID packetId) {
        this.packetId = packetId;
    }

    public UUID getPacketId() {
        return this.packetId;
    }
}