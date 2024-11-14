package me.fbiflow.gameengine.service.transfer.impl;

import me.fbiflow.gameengine.model.transfer.packet.Packet;
import me.fbiflow.gameengine.service.transfer.DataReceiver;

import java.util.ArrayList;
import java.util.List;

public class SocketDataReceiver extends DataReceiver {

    private final List<Packet> dataList = new ArrayList<>();

    @Override
    public Packet getData() {
        return null;
    }

    @Override
    public void setup() {

    }

    @Override
    public void onDataReceive(Packet packet) {

    }
}
