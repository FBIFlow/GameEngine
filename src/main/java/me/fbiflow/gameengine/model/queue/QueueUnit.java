package me.fbiflow.gameengine.model.queue;

import me.fbiflow.gameengine.model.game.Game;
import me.fbiflow.gameengine.model.wrapper.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QueueUnit {

    private Player owner;
    private Class<? extends Game> game;
    private Player[] players;

    public QueueUnit(Class<? extends Game> game) {
        this.game = game;
        this.players = new Player[getMaxPlayers(game)];
    }

    public QueueUnit(Class<? extends Game> game, Player owner) {
        this.owner = owner;
        this.game = game;
        this.players = new Player[getMaxPlayers(game)];
    }

    public Player getOwner() {
		return this.owner;
    } 

	public Game getGame() {
        return this.game;
    } 




    private int getMaxPlayers(Class<? extends Game> game) {
        try {
            return (int) game.getMethod("getMaxPlayers").invoke(game);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
