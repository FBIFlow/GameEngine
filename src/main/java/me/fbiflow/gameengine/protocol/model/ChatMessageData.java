package me.fbiflow.gameengine.protocol.model;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;

import java.io.Serializable;
import java.util.List;

public class ChatMessageData implements Serializable {

    private final String message;
    private final List<String> receivers;

    public ChatMessageData(String message, List<String> receivers) {
        this.message = message;
        this.receivers = receivers;
    }

    public String getMessage() {
        return this.message;
    }

    public List<String> getReceivers() {
        return this.receivers;
    }
}