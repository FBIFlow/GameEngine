package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.util.UUID;

public abstract class DataSender extends DataInteractUnit {

    public abstract void send(UUID receiverUUID, Packet packet);

}