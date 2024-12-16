package me.fbiflow.gameengine.core.model.wrapper.internal;

public interface PlayerManager<T> {

    Player getPlayer(String name);

    Player getPlayer(T serverPlayer);
}