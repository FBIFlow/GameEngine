package me.fbiflow.remapped.protocol.common;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.util.List;

public class PacketLoop {

    private List<Packet> packets;

    public PacketLoop(List<Packet> packets) {
        this.packets = packets;
    }

    public void add(Packet packet) {
        if (packets.contains(packet)) {
            throw new RuntimeException("already contains this packet");
        }
        packets.add(packet);
    }

    public Packet poll() {
        return packets.removeFirst();
    }
}