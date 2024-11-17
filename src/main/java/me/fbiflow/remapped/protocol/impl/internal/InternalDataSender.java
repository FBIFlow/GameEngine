package me.fbiflow.remapped.protocol.impl.internal;

import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.DataSender;
import me.fbiflow.remapped.protocol.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InternalDataSender extends DataSender {

    private final Map<UUID, DataReceiver> dataReceivers;

    public InternalDataSender() {
        this.dataReceivers = new HashMap<>();
    }

    @Override
    public void send(UUID receiverUUID, Packet packet) {
        dataReceivers.get(receiverUUID).onReceive(packet);
    }
}