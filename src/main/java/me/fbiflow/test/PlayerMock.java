package me.fbiflow.test;


import me.fbiflow.remapped.core.model.wrapper.internal.Player;
import me.fbiflow.remapped.util.LoggerUtil;

import java.util.*;

public class PlayerMock implements Player {

    transient private final LoggerUtil logger = new LoggerUtil(" | [PlayerMock] -> ");

    private static final Map<String ,PlayerMock> playerExists = new HashMap<>();

    private final String name;

    private PlayerMock(String name) {
        this.name = name;
    }

    public static PlayerMock getPlayer(String name) {
        PlayerMock player = playerExists.get(name);
        if (player != null) {
            return player;
        }
        player = new PlayerMock(name);
        playerExists.put(name, player);
        return player;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getPermissions() {
        return List.of("default");
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerMock)) {
            return false;
        }
        return Objects.equals(this.name, ((PlayerMock) obj).name);
    }
}
