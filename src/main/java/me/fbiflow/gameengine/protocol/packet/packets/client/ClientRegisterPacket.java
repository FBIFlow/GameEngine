package me.fbiflow.gameengine.protocol.packet.packets.client;

import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;

public class ClientRegisterPacket extends AbstractPacket {

    private final ClientType clientType;

    public ClientRegisterPacket(ClientType clientType) {
        this.clientType = clientType;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    @Override
    public String toString() {
        return "ClientRegisterPacket{" +
                "clientType=" + clientType +
                '}';
    }
}