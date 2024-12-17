package me.fbiflow.gameengine.core.model.wrapper.internal.manager;

import me.fbiflow.gameengine.core.model.wrapper.internal.Player;

public interface PlayerManager {

    Player getPlayer(String name);

    Player getPlayer(Object serverPlayerImpl);

}
