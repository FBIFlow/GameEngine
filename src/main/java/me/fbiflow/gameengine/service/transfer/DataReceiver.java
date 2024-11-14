package me.fbiflow.gameengine.service.transfer;

import me.fbiflow.gameengine.model.transfer.packet.Packet;

public abstract class DataReceiver {

    public abstract Packet getData();

    public abstract void setup();

    public abstract void onDataReceive(Packet packet);

}