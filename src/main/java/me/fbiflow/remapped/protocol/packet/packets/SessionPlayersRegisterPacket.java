package me.fbiflow.remapped.protocol.packet.packets;

import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

import java.util.List;
import java.util.UUID;

//отправить игроков, которых должен принять SessionHolder и направить в необходимый мир сессии
public class SessionPlayersRegisterPacket extends AbstractPacket {

    private final List<Player> players;
    private final UUID packetId;

    public SessionPlayersRegisterPacket(List<Player> players, UUID packetId) {
        this.players = players;
        this.packetId = packetId;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public UUID getPacketId() {
        return this.packetId;
    }

    public UUID getConnectionKey() {
        return this.packetId;
    }
}