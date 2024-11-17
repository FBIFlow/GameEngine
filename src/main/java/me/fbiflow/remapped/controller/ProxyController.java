package me.fbiflow.remapped.controller;

import me.fbiflow.remapped.protocol.DataReceiver;
import me.fbiflow.remapped.protocol.DataSender;

import java.util.ArrayList;
import java.util.List;

public class ProxyController {

    private final DataSender dataSender;
    private final List<DataReceiver> dataReceivers = new ArrayList<>();

    public ProxyController(DataSender dataSender) {
        this.dataSender = dataSender;
    }

    private void start() {
        dataReceivers.get(1).
    }

}