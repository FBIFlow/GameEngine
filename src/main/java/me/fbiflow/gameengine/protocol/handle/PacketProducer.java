package me.fbiflow.gameengine.protocol.handle;

import me.fbiflow.gameengine.protocol.packet.Packet;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.*;

public class PacketProducer {

    private static Map<Object, PacketProducer> producers = new HashMap<>();

    private final Map<Socket, List<Packet>> receivedPackets = new HashMap<>();

    public static PacketProducer of(Object link) {
        if (producers.containsKey(link)) {
            return producers.get(link);
        }
        PacketProducer producer = new PacketProducer();
        producers.put(link, producer);
        return producer;
    }

    public void produce(Packet packet, Socket sender) {
        List<Packet> packetList = receivedPackets.getOrDefault(sender, new ArrayList<>());
        if (packetList.contains(packet)) {
            throw new RuntimeException("packet already exists");
        }
        packetList.add(packet);
        receivedPackets.put(sender, packetList);
    }

    @Nullable
    public Map<Socket, Packet> pull() {
        if (receivedPackets.isEmpty()) {
            return null;
        }
        for (Socket socket : receivedPackets.keySet()) {
            List<Packet> packetList = receivedPackets.get(socket);
            if (packetList.isEmpty()) {
                continue;
            }
            Packet packet = packetList.remove(0);
            return Map.of(socket, packet);
        }
        return null;
    }
}