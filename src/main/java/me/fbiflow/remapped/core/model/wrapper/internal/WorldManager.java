package me.fbiflow.remapped.core.model.wrapper.internal;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class WorldManager {

    private static Map<String, World> worlds;
    private Class<? extends World> worldType;

    public WorldManager(Class<? extends World> worldType) {
        this.worldType = worldType;
    }

    public World createWorld(String id) {
        Set<String> ids = worlds.keySet();
        if (ids.contains(id)) {
            throw new RuntimeException("World with this id already exists");
        }
        World world = null;
        try {
            Constructor<? extends World> constructor = worldType.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            world = constructor.newInstance(id);
            constructor.setAccessible(false);
            worlds.put(id, world);
            return world;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public World getWorld(String id) {
        return worlds.get(id);
    }
}