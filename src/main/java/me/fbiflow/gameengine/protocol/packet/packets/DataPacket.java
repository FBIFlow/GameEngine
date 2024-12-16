package me.fbiflow.gameengine.protocol.packet.packets;

import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

public class DataPacket extends AbstractPacket {

    private final List<Object> data;

    public DataPacket() {
        this.data = new ArrayList<>();
        System.out.println("time before creating packet: " + System.currentTimeMillis());
        for (int i = 0; i < 10000000; i++) {
            this.data.add("STRING!:" + String.valueOf(i));
        }
        System.out.println("time after creating packet: " + System.currentTimeMillis());
    }

    public List<Object> getData() {
        return this.data;
    }
}