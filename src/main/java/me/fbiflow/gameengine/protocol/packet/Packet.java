package me.fbiflow.gameengine.protocol.packet;

import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public record Packet(UUID uuid, byte[] abstractPacket, Class<? extends AbstractPacket> packetClass) implements Serializable {

    public static Packet of(AbstractPacket packet) {
        try {
            return new Packet(UUID.randomUUID(), SerializeUtil.serialize(packet), packet.getClass());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return ((Packet) o).uuid == this.uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, Arrays.hashCode(abstractPacket), packetClass);
    }
}