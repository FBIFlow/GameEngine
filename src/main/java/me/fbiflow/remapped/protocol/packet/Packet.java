package me.fbiflow.remapped.protocol.packet;

import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;

import java.io.Serializable;

public record Packet(byte[] data, Class<? extends AbstractPacket> packetClass) implements Serializable {

}