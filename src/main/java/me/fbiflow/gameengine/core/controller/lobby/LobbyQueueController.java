package me.fbiflow.gameengine.core.controller.lobby;

import me.fbiflow.gameengine.core.controller.event.EventHandleService;
import me.fbiflow.gameengine.core.controller.event.EventHandler;
import me.fbiflow.gameengine.core.controller.event.EventListener;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerJoinEvent;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerLeaveEvent;
import me.fbiflow.gameengine.protocol.communication.Client;
import me.fbiflow.gameengine.protocol.handle.PacketHandleService;
import me.fbiflow.gameengine.protocol.handle.PacketListener;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.PlayerRemoveControllerPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.PlayerSetControllerPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

public class LobbyQueueController implements PacketListener, EventListener {

    private final LoggerUtil logger = new LoggerUtil("| [LobbyQueueController] ->");

    private final LobbyController lobbyController;

    private final Client serverConnection;

    public LobbyQueueController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
        this.serverConnection = lobbyController.getConnection();
        PacketHandleService.getInstance().registerListener(lobbyController.getPacketProducer(), this);
        EventHandleService.getInstance().registerListener(lobbyController.getEventProducer(), this);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        System.err.println("player join event handled");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        serverConnection.sendPacket(Packet.of(
                new PlayerSetControllerPacket(serverConnection.getClientId(), event.getPlayer())
        ));
    }

    @EventHandler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        serverConnection.sendPacket(Packet.of(
                new PlayerRemoveControllerPacket(serverConnection.getClientId(), event.getPlayer())));
    }

}