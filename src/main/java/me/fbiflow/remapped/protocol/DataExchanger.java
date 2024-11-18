package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;

import java.util.UUID;

public abstract class DataExchanger {

    public DataExchanger() {

    }

    public abstract void start();

    public abstract void sendData(Packet packet);

    public abstract void handleData(Packet receivedPacket);

}