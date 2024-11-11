package me.fbiflow.gameengine.service;

import me.fbiflow.gameengine.model.queue.QueueUnit;
import me.fbiflow.gameengine.model.wrapper.Player;

public class QueueService {

    private QueueUnit[] queue;

    public QueueService() {
        this.queue = new QueueUnit[5];
    }

    public boolean isPlayerInQueue(Player player) {
        return false;
    }

    public void addToQueue(Player player) {

    }

}