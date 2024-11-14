package me.fbiflow.gameengine.service.consumer;

import me.fbiflow.gameengine.model.game.Game;
import me.fbiflow.gameengine.model.QueueUnit;
import me.fbiflow.gameengine.model.wrapper.Player;
import me.fbiflow.gameengine.service.transfer.DataReceiver;
import me.fbiflow.gameengine.service.transfer.DataSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConsumerService {

    private final List<QueueUnit> queue = new ArrayList<>();

    private final DataSender dataSender;
    private final DataReceiver dataReceiver;

    public ConsumerService(DataSender dataSender, DataReceiver dataReceiver) {
        this.dataReceiver = dataReceiver;
        this.dataSender = dataSender;
    }

    public List<QueueUnit> getQueue() {
        return this.queue;
    }

    public int getUsersInQueue() {
        int count = 0;
        for (QueueUnit unit : queue) {
            count += unit.getPlayersCount();
        }
        return count;
    }

    public void addToQueue(Player player, Class<? extends Game> gameType) {
        if (isPlayerInQueue(player)) {
            System.out.println("player is already in queue");
            return;
        }
        for (QueueUnit unit : queue) {
            if (unit.getGameType() == gameType && unit.hasFreeSlot()) {
                unit.addPlayer(player);
                return;
            }
        }
        addUnit(gameType).addPlayer(player);
    }

    public boolean isPlayerInQueue(@NotNull Player player) {
        for (QueueUnit unit : queue) {
            if (unit.contains(player)) {
                return true;
            }
        }
        return false;
    }

    private QueueUnit addUnit(Class<? extends Game> gameType) {
        QueueUnit unit = new QueueUnit(gameType);
        queue.add(unit);
        return unit;
    }
}