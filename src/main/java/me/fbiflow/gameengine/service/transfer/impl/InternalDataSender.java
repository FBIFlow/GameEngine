package me.fbiflow.gameengine.service.transfer.impl;

import me.fbiflow.gameengine.model.transfer.Packet;
import me.fbiflow.gameengine.service.transfer.DataReceiver;
import me.fbiflow.gameengine.service.transfer.DataSender;

public class InternalDataSender implements DataSender {

    private DataReceiver dataReceiver;

    public InternalDataSender(DataReceiver dataReceiver) {
        this.dataReceiver = dataReceiver;
    }

    @Override
    public void sendData(Packet data) {
        dataReceiver.putData(data);
    }
}
