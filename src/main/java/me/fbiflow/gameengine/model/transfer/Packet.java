package me.fbiflow.gameengine.model.transfer;

import me.fbiflow.gameengine.model.transfer.data.PacketData;

import java.io.Serializable;
import java.util.UUID;

public record Packet(PacketType packetType, UUID id, PacketData data) implements Serializable {

}