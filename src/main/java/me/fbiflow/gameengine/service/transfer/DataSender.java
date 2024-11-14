package me.fbiflow.gameengine.service.transfer;

import me.fbiflow.gameengine.model.transfer.Packet;

public interface DataSender {

    void sendData(Packet data);

}