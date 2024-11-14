package me.fbiflow.gameengine.service.transfer.impl;

import me.fbiflow.gameengine.model.transfer.Packet;
import me.fbiflow.gameengine.service.transfer.DataReceiver;

import java.util.ArrayList;
import java.util.List;

public class InternalDataReceiver implements DataReceiver {

    private final List<Packet> dataList = new ArrayList<>();

    @Override
    public Packet getData() {
        return dataList.removeFirst();
    }

    @Override
    public void putData(Packet data) {
        dataList.add(data);
    }
}
