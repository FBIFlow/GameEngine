package me.fbiflow.remapped.model.queue;

import me.fbiflow.remapped.model.exceptions.PlayerNotInQueueException;
import me.fbiflow.remapped.model.game.Game;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class QueueManager {

    private final ArrayList<QueueUnit> queue;

    public QueueManager() {
        this.queue = new ArrayList<>();
    }

    private void addToQueue(QueueUnit queueUnit) {
        if (queue.contains(queueUnit)) {
            throw new RuntimeException("QueueUnit already in queue");
        }
        queue.add(queueUnit);
    }

    private void removeFromQueue(QueueUnit queueUnit) {
        if (!queue.contains(queueUnit)) {
            throw new RuntimeException("QueueUnit isn`t in queue");
        }
        queue.remove(queueUnit);
    }

    /**
     * Manually creating QueueUnit by player
     * @param owner who creates
     */
    private void createQueueManually(Player owner) {
        QueueUnit queueUnit = createQueue();
        queueUnit.addMember(owner);
        queueUnit.setOwner(owner);
    }

    /**
     * Create new instance of QueueUnit and add it to queue
     * @return created instance of QueueUnit
     */
    private QueueUnit createQueue() {
        QueueUnit queueUnit = new QueueUnit();
        addToQueue(queueUnit);
        return queueUnit;
    }

    private QueueUnit createQueue(Class<? extends Game> gameType) {
        QueueUnit queueUnit = new QueueUnit();
        queueUnit.setGameType(gameType);
        addToQueue(queueUnit);
        return queueUnit;
    }

    private void removeQueue(QueueUnit queueUnit) {
        queue.remove(queueUnit);
    }

    /**
     * Leave queue
     * @param player who leaves
     */
    private void leaveQueue(Player player) {
        QueueUnit queueUnit = getQueueUnit(player);
        if (queueUnit == null) {
            throw new PlayerNotInQueueException();
        }

        queueUnit.removeMember(player);

        if (queueUnit.isEmpty()) {
            removeQueue(queueUnit);
            return;
        }

        if (queueUnit.isOwner(player)) {
            queueUnit.setOwner(queueUnit.getMembers().getFirst());
        }

    }

    private void joinQueue(Player player, Class<? extends Game> gameType) {
        QueueUnit queueUnit = Optional.ofNullable(getFreeQueueUnit(gameType)).orElse(createQueue(gameType));
        queueUnit.addMember(player);
    }

    @Nullable
    private QueueUnit getFreeQueueUnit(Class<? extends Game> gameType) {
        for (QueueUnit queueUnit : queue) {
            if (!queueUnit.isFull() && queueUnit.getGameType() == gameType) {
                return queueUnit;
            }
        }
        return null;
    }

    @Nullable
    private QueueUnit getQueueUnit(Player player) {
        for (QueueUnit queueUnit : queue) {
            if (queueUnit.getMembers().contains(player)) {
                return queueUnit;
            }
        }
        return null;
    }
}