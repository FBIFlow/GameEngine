package me.fbiflow.remapped.protocol.impl.internal;

import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.common.PacketLoop;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;

public class InternalDataReceiver extends DataReceiver {

    private final PacketLoop packetLoop;

    public InternalDataReceiver() {
        this.packetLoop = new PacketLoop();
    }

    @Override
    public Packet poll() {
        return packetLoop.poll();
    }

    @Override
    public void onReceive(byte[] packet) {
        try {
            packetLoop.add(((Packet) SerializeUtil.deserialize(packet)));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}