package me.fbiflow.remapped.model.wrapper.internal;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {

    String getName();

    List<String> getPermissions();

}
