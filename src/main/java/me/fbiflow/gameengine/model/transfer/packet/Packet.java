package me.fbiflow.gameengine.model.transfer.packet;

import me.fbiflow.gameengine.model.transfer.data.PacketData;

import java.io.Serializable;
import java.util.UUID;

public class Packet implements Serializable {

    public final PacketType packetType;
    public final UUID senderUUID;
    public final PacketData packetData;

    public Packet(PacketType packetType, UUID senderUUID, PacketData packetData) {
        this.packetType = packetType;
        this.senderUUID = senderUUID;
        this.packetData = packetData;
    }

    public PacketType getPacketType() {
        return this.packetType;
    }

    public UUID getSenderUUID() {
        return this.senderUUID;
    }

    public PacketData getPacketData() {
        return this.packetData;
    }
}