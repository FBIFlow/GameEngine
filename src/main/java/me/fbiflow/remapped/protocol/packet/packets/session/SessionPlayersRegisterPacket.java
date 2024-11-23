package me.fbiflow.remapped.protocol.packet.packets.session;

import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

import java.util.List;
import java.util.UUID;

//отправить игроков, которых должен принять SessionHolder и направить в необходимый мир сессии
public class SessionPlayersRegisterPacket extends AbstractPacket {

    private final UUID packetId;
    private final List<Player> players;

    public SessionPlayersRegisterPacket(List<Player> players, UUID packetId) {
        this.players = players;
        this.packetId = packetId;
    }

    public UUID getPacketId() {
        return this.packetId;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}