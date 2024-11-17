package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;

public abstract class DataReceiver extends DataInteractUnit {

    public abstract void onReceive(Packet packet);

}