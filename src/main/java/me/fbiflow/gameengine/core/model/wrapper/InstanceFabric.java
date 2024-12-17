package me.fbiflow.gameengine.core.model.wrapper;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.core.model.wrapper.internal.view.BossbarView;
import me.fbiflow.gameengine.core.model.wrapper.internal.view.ChatView;
import me.fbiflow.gameengine.core.model.wrapper.internal.view.SidebarView;

import java.lang.reflect.InvocationTargetException;

public class InstanceFabric {

    private final ServerContainer serverContainer;

    public InstanceFabric(ServerContainer serverContainer) {
        this.serverContainer = serverContainer;
    }

    public BossbarView createBossbar() throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        return serverContainer.bossbarView.getDeclaredConstructor().newInstance();
    }

    public ChatView createChatView() throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        return serverContainer.chatView.getDeclaredConstructor().newInstance();
    }

    public SidebarView createSidebarView() throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        return serverContainer.sidebarView.getDeclaredConstructor().newInstance();
    }

    public Player getPlayer(String name) {
        switch (serverContainer.getContainerType()) {
            case MINESTOM -> {

                return null;
            }
            case BUKKIT -> {
                return null;
            }
            default -> {
                return null;
            }
        }
    }

}