package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;

public abstract class DataReceiver extends DataInteractUnit {

    public abstract Packet poll();

    public abstract void onReceive(byte[] packet);

}