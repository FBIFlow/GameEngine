package me.fbiflow.gameengine.core.model.game;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractGame {

    public AbstractGame() {
        getMaxPartyPlayers().forEach((_, maxPartyPlayers) -> {
            if (maxPartyPlayers > getMaxPlayers()) {
                throw new IllegalStateException("max party players can not be greater then max game players");
            }
        });
    }

    public abstract String getId();

    public abstract int getMaxPlayers();

    public abstract int getRequiredPlayers();

    /**
     * @return map <br>
     * k - party-owner-permission <br>
     * v - max-party-players
     */
    public abstract Map<String, Integer> getMaxPartyPlayers();

    public abstract void onInit();

    public abstract void onStart();

    public abstract void onTick();

    public abstract void onEnd();

    public abstract void onRemove();

    @Override
    public int hashCode() {
        return Objects.hash(getRequiredPlayers(), getMaxPlayers(), getMaxPartyPlayers(), this.getClass().getSimpleName());
    }
}