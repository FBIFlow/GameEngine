package me.fbiflow.gameengine.model.queue;

import me.fbiflow.gameengine.model.game.Game;
import me.fbiflow.gameengine.model.wrapper.Player;

public class QueueUnit {

    private Player owner;
    private Class<? extends Game> game;
    private Player[] players;

    public QueueUnit(Class<? extends Game> game) {
        this.game = game;
        this.players = new Player[game.countOfPlayer()];
    }

    public QueueUnit(Class<? extends Game> game, Player owner) {
        this.owner = owner;
        this.game = game;
        this.players = new Player[game.countOfPlayer()];
    }

}