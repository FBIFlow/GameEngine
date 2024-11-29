package me.fbiflow.gameengine.core.model.game;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class GameManager {

    public static int getMaxPlayers(Class<? extends AbstractGame> gameType) {
        return createInstance(gameType).getMaxPlayers();
    }

    public static int getRequiredPlayers(Class<? extends AbstractGame> gameType) {
        return createInstance(gameType).getRequiredPlayers();
    }

    public static Map<String, Integer> getMaxPartyPlayers(Class<? extends AbstractGame> gameType) {
        return createInstance(gameType).getMaxPartyPlayers();

    }

    private static AbstractGame createInstance(Class<? extends AbstractGame> gameType) {
        try {
            return gameType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
