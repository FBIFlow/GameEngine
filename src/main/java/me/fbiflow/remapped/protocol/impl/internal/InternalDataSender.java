package me.fbiflow.remapped.protocol.impl.internal;

import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.DataSender;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InternalDataSender extends DataSender {

    private DataReceiver defaultReceiver;
    private final Map<UUID, DataReceiver> dataReceivers;

    public InternalDataSender() {
        this.dataReceivers = new HashMap<>();
    }

    public void setDefaultReceiver(DataReceiver defaultReceiver) {
        this.defaultReceiver = defaultReceiver;
    }

    @Override
    public void setDefaultReceiver(Object receiverData) {
        if (!(receiverData instanceof DataReceiver)) {
            throw new IllegalArgumentException("receiverData may be instance of DataReceiver");
        }
        this.defaultReceiver = (DataReceiver) receiverData;
    }

    @Override
    public boolean hasDefaultReceiver() {
        return defaultReceiver != null;
    }

    public void setDataReceivers(DataReceiver... dataReceivers) {
        for (DataReceiver dataReceiver : dataReceivers) {
            this.dataReceivers.put(dataReceiver.uuid, dataReceiver);
        }
    }
 //TODO: MAKE DATARECEIVER AND DATASENDER TO 1 SERVER OBJECT
    @Override
    public void send(UUID receiverUUID, Packet packet) {
        try {
            byte[] serialized = SerializeUtil.serialize(packet);
            DataReceiver dataReceiver = dataReceivers.get(receiverUUID);
            if (dataReceiver == null) {
                System.out.println("receiver is null");
                dataReceiver = defaultReceiver;
            }
            System.out.println("default receiver is: " + defaultReceiver);
            dataReceiver.onReceive(serialized);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}