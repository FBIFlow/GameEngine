package me.fbiflow.gameengine.protocol.packet;

import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.io.Serializable;

public record Packet(byte[] data, Class<? extends AbstractPacket> packetClass) implements Serializable {

    public static Packet of(AbstractPacket packet) {
        try {
            return new Packet(SerializeUtil.serialize(packet), packet.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}