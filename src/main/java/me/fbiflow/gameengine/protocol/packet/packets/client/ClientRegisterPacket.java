package me.fbiflow.gameengine.protocol.packet.packets.client;

import me.fbiflow.gameengine.protocol.enums.ClientType;
import me.fbiflow.gameengine.protocol.packet.AbstractPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientRegisterPacket extends AbstractPacket {

    private final UUID clientId;
    private final ClientType clientType;
    /**
     * k - game id <br>
     * v - game hash
     */
    private Map<String, Integer> allowedGames = null;

    public ClientRegisterPacket(UUID clientId, ClientType clientType, @Nullable Map<String, Integer> allowedGames) {
        this.clientId = clientId;
        this.clientType = clientType;
        if (clientType != ClientType.SESSION_CONTROLLER) {
            return;
        }
        if (allowedGames == null) {
            throw new IllegalStateException("allowedGames can not be null when clientType is SESSION_CONTROLLER");
        }
        this.allowedGames = allowedGames;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    public Map<String, Integer> getAllowedGames() {
        return this.allowedGames;
    }

    @Override
    public String toString() {
        return "ClientRegisterPacket{" +
                "clientType=" + clientType +
                ", allowedGames=" + (this.clientType == ClientType.LOBBY_CONTROLLER ? "–" : allowedGames)   +
                '}';
    }
}