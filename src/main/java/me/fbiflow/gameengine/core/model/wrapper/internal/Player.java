package me.fbiflow.gameengine.core.model.wrapper.internal;

import net.kyori.adventure.text.Component;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {

    String getName();

    List<String> getPermissions();

    void sendMessage(Component message);

}
