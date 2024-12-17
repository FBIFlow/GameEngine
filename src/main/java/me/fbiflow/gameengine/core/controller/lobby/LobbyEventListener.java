package me.fbiflow.gameengine.core.controller.lobby;

import me.fbiflow.gameengine.core.controller.event.EventHandleService;
import me.fbiflow.gameengine.core.controller.event.EventHandler;
import me.fbiflow.gameengine.core.controller.event.EventListener;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerJoinEvent;
import me.fbiflow.gameengine.core.model.wrapper.internal.event.events.PlayerLeaveEvent;
import me.fbiflow.gameengine.protocol.communication.Client;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.protocol.packet.packets.client.PlayerRemoveControllerPacket;
import me.fbiflow.gameengine.protocol.packet.packets.client.PlayerSetControllerPacket;
import me.fbiflow.gameengine.util.LoggerUtil;

public class LobbyEventListener implements EventListener {

    private final LobbyController lobbyController;
    private final Client serverConnection;

    private final LoggerUtil logger = new LoggerUtil("| [LobbyEventListener] ->");

    public LobbyEventListener(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
        this.serverConnection = lobbyController.getConnection();
        EventHandleService.getInstance().registerListener(lobbyController.getEventProducer(), this);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        logger.log("player join event handled");
        serverConnection.sendPacket(Packet.of(
                new PlayerSetControllerPacket(serverConnection.getClientId(), event.getPlayer())
        ));
    }

    @EventHandler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        System.err.println("player leave event handled");
        serverConnection.sendPacket(Packet.of(
                new PlayerRemoveControllerPacket(serverConnection.getClientId(), event.getPlayer())));
    }

}
