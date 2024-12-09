package me.fbiflow.gameengine.protocol.packet.packets.client;

import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

public class ClientUnregisterPacket extends AbstractPacket {

    private final ClientType clientType;

    public ClientUnregisterPacket(ClientType clientType) {
        this.clientType = clientType;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    @Override
    public String toString() {
        return "ClientUnregisterPacket{" +
                "clientType=" + clientType +
                '}';
    }
}