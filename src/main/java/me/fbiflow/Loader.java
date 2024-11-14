package me.fbiflow;

import me.fbiflow.gameengine.model.transfer.Packet;
import me.fbiflow.gameengine.model.transfer.PacketType;
import me.fbiflow.gameengine.model.transfer.data.PacketData;

import java.io.*;
import java.util.Arrays;
import java.util.UUID;

public class Loader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Packet packet = new Packet(PacketType.DEFAULT, UUID.randomUUID(), );
        System.out.printf("packet: {%s | %s | %s}\n", packet.packetType(), packet.id(), packet.data());
        byte[] bytes = serialize(packet);
        System.out.printf("bytes: %s\n", Arrays.toString(bytes));
        Packet o = (Packet) deserialize(bytes);
        System.out.printf("deserialized: {%s | %s | %s}\n", o.packetType(), o.id(), o.data());
    }

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }
}