package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.util.UUID;

public abstract class DataSender extends DataInteractUnit {

    public abstract void setDefaultReceiver(Object receiverData);

    public abstract boolean hasDefaultReceiver();

    public abstract void send(UUID receiverUUID, Packet packet);

}