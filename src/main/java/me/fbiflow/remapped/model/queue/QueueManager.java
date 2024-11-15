package me.fbiflow.remapped.model.queue;

import me.fbiflow.remapped.model.exceptions.PlayerNotInQueueException;
import me.fbiflow.remapped.model.game.Game;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class QueueManager {

    private final ArrayList<QueueUnit> queue;

    public QueueManager() {
        this.queue = new ArrayList<>();
    }

    /**
     * Add QueueUnit to queue
     * @param queueUnit QueueUnit to add
     */
    private void addQueueUnitToQueue(QueueUnit queueUnit) {
        if (queue.contains(queueUnit)) {
            throw new RuntimeException("QueueUnit already in queue");
        }
        queue.add(queueUnit);
    }

    /**
     * Remove QueueUnit from queue
     * @param queueUnit QueueUnit to remove
     */
    private void removeQueueUnitFromQueue(QueueUnit queueUnit) {
        if (!queue.contains(queueUnit)) {
            throw new RuntimeException("QueueUnit isn`t in queue");
        }
        queue.remove(queueUnit);
    }

    /**
     * Create new instance of QueueUnit and add it to queue
     * @return created instance of QueueUnit
     */
    private QueueUnit createQueueUnit() {
        QueueUnit queueUnit = QueueUnit.newInstance();
        addQueueUnitToQueue(queueUnit);
        return queueUnit;
    }

    /**
     * Remove player from queue
     * @param player who leaves
     */
    private void leaveQueue(Player player) {
        QueueUnit queueUnit = getPlayerQueueUnit(player);
        if (queueUnit == null) {
            throw new PlayerNotInQueueException();
        }

        queueUnit.removeMember(player);

        if (queueUnit.isEmpty()) {
            removeQueueUnitFromQueue(queueUnit);
            return;
        }

        if (queueUnit.isOwner(player)) {
            queueUnit.setOwner(queueUnit.getMembers().getFirst());
        }

    }

    /**
     * Add player to queue, try find existing QueueUnit or create new
     * @param player who joins
     * @param gameType type of required game
     */
    private void joinQueue(Player player, Class<? extends Game> gameType) {
        QueueUnit queueUnit = getFreeQueueUnit(gameType);
        if (queueUnit == null) {
            queueUnit = createQueueUnit();
            queueUnit.setGameType(gameType);
        }
        queueUnit.addMember(player);
    }

    /**
     * Find QueueUnit with specified game type and existing slot for player
     * @param gameType type of game
     * @return QueueUnit if exists
     */
    @Nullable
    private QueueUnit getFreeQueueUnit(Class<? extends Game> gameType) {
        for (QueueUnit queueUnit : queue) {
            if (!queueUnit.isFull() && queueUnit.getGameType() == gameType) {
                return queueUnit;
            }
        }
        return null;
    }

    /**
     * Find QueueUnit with specified game type and existing slots for players
     * @param gameType type of game
     * @param freeSlots count of free slots
     * @return QueueUnit if exists
     */
    @Nullable
    private QueueUnit getFreeQueueUnit(Class<? extends Game> gameType, int freeSlots) {
        for (QueueUnit queueUnit : queue) {
            if (queueUnit.getMembers().size() + freeSlots <= queueUnit.getMaxPlayers() && queueUnit.getGameType() == gameType) {
                return queueUnit;
            }
        }
        return null;
    }

    /**
     * Find QueueUnit by Player
     * @param player who`s QueueUnit to find
     * @return QueueUnit in which player or null if player isn`t in queue
     */
    @Nullable
    private QueueUnit getPlayerQueueUnit(Player player) {
        for (QueueUnit queueUnit : queue) {
            if (queueUnit.getMembers().contains(player)) {
                return queueUnit;
            }
        }
        return null;
    }
}