package me.fbiflow.gameengine.service.consumer;

import me.fbiflow.gameengine.model.;
import me.fbiflow.gameengine.model.game.Game;
import me.fbiflow.gameengine.model.QueueUnit;
import me.fbiflow.gameengine.model.wrapper.Player;
import me.fbiflow.gameengine.service.transfer.DataReceiver;
import me.fbiflow.gameengine.service.transfer.DataSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ConsumerService {

    private final List<QueueUnit> queue = new ArrayList<>();

    private final DataSender dataSender;
    private final DataReceiver producerReceiver;
    private final DataReceiver executorReceiver;

    public ConsumerService(DataSender dataSender, DataReceiver producerReceiver, DataReceiver executorReceiver) {
        this.producerReceiver = producerReceiver;
        this.dataSender = dataSender;
        this.executorReceiver = executorReceiver;
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

    public void removeFromQueue(Player player) {
        QueueUnit unit = getPlayerQueueUnit(player);
        if (unit == null) {
            System.out.println("player isn`t in queue");
            return;
        }
        unit.removePlayer(player);
        if (unit.getPlayersCount() == 0) {
            queue.remove(unit);
        }
    }

    public boolean isPlayerInQueue(Player player) {
        for (QueueUnit unit : queue) {
            if (unit.contains(player)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public QueueUnit getPlayerQueueUnit(Player player) {
        if (!isPlayerInQueue(player)) {
            return null;
        }
        for (QueueUnit unit : queue) {
            if (unit.contains(player)) {
                return unit;
            }
        }
        return null;
    }

    private QueueUnit addUnit(Class<? extends Game> gameType) {
        QueueUnit unit = new QueueUnit(gameType);
        queue.add(unit);
        return unit;
    }
}