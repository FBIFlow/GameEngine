package me.fbiflow.gameengine.protocol.packet;

import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.io.Serializable;

public abstract class AbstractPacket implements Serializable {

    public byte[] toByteArray() {
        try {
            return SerializeUtil.serialize(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}