package me.fbiflow.remapped.protocol.impl.internal;

import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.common.PacketLoop;
import me.fbiflow.remapped.protocol.packet.Packet;

public class InternalDataReceiver extends DataReceiver {

    private final PacketLoop packetLoop;

    public InternalDataReceiver(PacketLoop packetLoop) {
        this.packetLoop = packetLoop;
    }

    public Packet handlePacket() {
        return packetLoop.poll();
    }

    @Override
    public void onReceive(Packet packet) {
        packetLoop.add(packet);
    }
}