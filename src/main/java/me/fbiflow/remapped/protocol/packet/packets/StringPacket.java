package me.fbiflow.remapped.protocol.packet.packets;

public class StringPacket extends AbstractPacket {

    public String string;

    public StringPacket(String string) {
        this.string = string;
    }
}