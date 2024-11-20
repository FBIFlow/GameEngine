package me.fbiflow.remapped.core.controller;

import me.fbiflow.remapped.protocol.communication.SocketDataClient;

public class SessionHolderController {




    private final SocketDataClient client;

    public SessionHolderController(SocketDataClient client) {
        this.client = client;
    }
}
