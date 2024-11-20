package me.fbiflow.remapped.core.model.game;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class GameManager {

    public static int getMaxPlayers(Class<? extends AbstractGame> gameType) {
        try {
            return gameType.getConstructor().newInstance().getMaxPlayers();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Integer> getMaxPartyPlayers(Class<? extends AbstractGame> gameType) {
        try {
            return gameType.getConstructor().newInstance().getMaxPartyPlayers();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
