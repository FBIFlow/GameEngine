package me.fbiflow.gameengine.core.model.game.games;

import me.fbiflow.gameengine.core.model.game.AbstractGame;

import java.util.Map;

public class Pillars extends AbstractGame {

    @Override
    public int getMaxPlayers() {
        return 8;
    }

    @Override
    public int getRequiredPlayers() {
        return 4;
    }

    @Override
    public Map<Integer, Integer> getPlayerTimerMap() {
        return Map.of(
                4, 10,
                6, 5
        );
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

    @Override
    public void onRemove() {

    }
}