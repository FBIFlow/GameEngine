package me.fbiflow.test;


import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.*;

public class PlayerMock implements Player {

    private static final Map<String ,PlayerMock> playerExists = new HashMap<>();

    private String name;

    public PlayerMock(String name) {
        this.name = name;
        for (Player x : playerExists.values()) {
            if (x.getName().equals(name)) {
                this.name = UUID.randomUUID().toString();
                System.out.println("name already exists, using random uuid: " + this.name);
            }
        }
        playerExists.put(this.name ,this);
    }

    public static PlayerMock getPlayer(String name) {
        PlayerMock player = playerExists.get(name);
        if (player == null) {
            throw new RuntimeException("could not to find a player with name: " + name);
        }
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

}
