package me.fbiflow.remapped.protocol.packet;

import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.util.SerializeUtil;

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