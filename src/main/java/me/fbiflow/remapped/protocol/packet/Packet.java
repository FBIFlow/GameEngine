package me.fbiflow.remapped.protocol.packet;

import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;

import java.io.Serializable;
import java.util.UUID;

public record Packet(UUID receiver, byte[] data, Class<? extends AbstractPacket> packetClass) implements Serializable {

}