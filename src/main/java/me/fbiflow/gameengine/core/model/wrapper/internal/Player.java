package me.fbiflow.gameengine.core.model.wrapper.internal;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {

    String getName();

    List<String> getPermissions();

}
