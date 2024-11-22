package me.fbiflow.remapped.protocol.packet.packets.session;

import me.fbiflow.remapped.core.model.game.AbstractGame;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;

import java.util.UUID;

//есть ли свободная сессия в SessionHolder?
public class SessionGetRequestPacket extends AbstractPacket {

    private final UUID packetId = UUID.randomUUID();
    private final Class<? extends AbstractGame> gameType;

    public SessionGetRequestPacket(Class<? extends AbstractGame> gameType) {
        this.gameType = gameType;
    }

    public UUID getPacketId() {
        return this.packetId;
    }

    public Class<? extends AbstractGame> getGameType() {
        return this.gameType;
    }
}