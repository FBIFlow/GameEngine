package me.fbiflow.remapped.model.game.games;

import me.fbiflow.remapped.model.game.AbstractGame;

import java.util.Map;

public class Pillars extends AbstractGame {

    @Override
    public int getMaxPlayers() {
        return 8;
    }

    @Override
    public Map<String, Integer> getMaxPartyPlayers() {
        return Map.of(
                "default", 4,
                "vip", 6,
                "admin", 8
        );
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {

    }
}
