package me.fbiflow.gameengine.service.transfer;

import me.fbiflow.gameengine.model.transfer.Packet;

public interface DataReceiver {

    Packet getData();

    void putData(Packet data);

}