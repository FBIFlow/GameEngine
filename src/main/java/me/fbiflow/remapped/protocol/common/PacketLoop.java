package me.fbiflow.remapped.protocol.common;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketLoop {

    private final List<Packet> packets = Collections.synchronizedList(new ArrayList<>());

    public void add(Packet packet) {
        System.out.println("called add");
        if (packets.contains(packet)) {
            throw new RuntimeException("already contains this packet");
        }
        packets.add(packet);
        System.out.println(packets);
    }

    public Packet poll() {
        if (packets.isEmpty()) {
            return null;
        }
        System.out.println("poll isn`t empty");
        Packet packet = packets.getFirst();
        if (packet != null) {
            packets.remove(0);
        }
        return packet;
    }
}