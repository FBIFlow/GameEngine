package me.fbiflow.remapped.model.queue;

import me.fbiflow.remapped.model.game.Game;
import me.fbiflow.remapped.model.party.Party;
import me.fbiflow.remapped.model.party.PartyManager;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class QueueManager {

    private final ArrayList<QueueItem> queue;
    private final PartyManager partyManager;

    public QueueManager(PartyManager partyManager) {
        this.queue = new ArrayList<>();
        this.partyManager = partyManager;
    }

    /**
     * Add player to queue, try find existing QueueItem or create new
     * @param player who joins
     * @param gameType type of required game
     */
    public void joinQueue(Player player, Class<? extends Game> gameType) {
        if (!partyManager.isPlayerInParty(player)) {
            QueueItem queueItem = getFreeQueueItem(gameType);
            if (queueItem == null) {
                queueItem = createQueueItem();
                queueItem.setGameType(gameType);
            }
            queueItem.getMembers().add(player);
            return;
        }
        Party party = partyManager.getByMember(player);
        if (party.getOwner() != player) {
            //TODO: send message (only owner can start queue)
            return;
        }
        if () {

        }
    }

    /**
     * Remove player from queue
     * @param player who leaves
     */
    public void leaveQueue(Player player) {
        QueueItem queueItem = getPlayerQueueItem(player);
        if (queueItem == null) {
            return;
        }

        queueItem.getMembers().remove(player);

        if (queueItem.isEmpty()) {
            removeQueueItemFromQueue(queueItem);
            return;
        }

        if (queueItem.isOwner(player)) {
            queueItem.setOwner(queueItem.getMembers().getFirst());
        }
    }

    /**
     * Add QueueItem to queue
     * @param queueItem QueueItem to add
     */
    private void addQueueItemToQueue(QueueItem queueItem) {
        if (queue.contains(queueItem)) {
            throw new RuntimeException("QueueItem already in queue");
        }
        queue.add(queueItem);
    }

    /**
     * Create new instance of QueueItem and add it to queue
     * @return created instance of QueueItem
     */
    private QueueItem createQueueItem() {
        QueueItem queueItem = QueueItem.newInstance();
        addQueueItemToQueue(queueItem);
        return queueItem;
    }

    /**
     * Remove QueueItem from queue
     * @param queueItem QueueItem to remove
     */
    private void removeQueueItemFromQueue(QueueItem queueItem) {
        if (!queue.contains(queueItem)) {
            throw new RuntimeException("QueueItem isn`t in queue");
        }
        queue.remove(queueItem);
    }

    /**
     * Find QueueItem with specified game type and existing slot for player
     * @param gameType type of game
     * @return QueueItem if exists
     */
    @Nullable
    private QueueItem getFreeQueueItem(Class<? extends Game> gameType) {
        for (QueueItem queueItem : queue) {
            if (!queueItem.isFull() && queueItem.getGameType() == gameType) {
                return queueItem;
            }
        }
        return null;
    }

    /**
     * Find QueueItem with specified game type and existing slots for players
     * @param gameType type of game
     * @param freeSlots count of free slots
     * @return QueueItem if exists
     */
    @Nullable
    private QueueItem getFreeQueueItem(Class<? extends Game> gameType, int freeSlots) {
        for (QueueItem queueItem : queue) {
            if (queueItem.getMembers().size() + freeSlots <= queueItem.getMaxPlayers() && queueItem.getGameType() == gameType) {
                return queueItem;
            }
        }
        return null;
    }

    /**
     * Find QueueItem by Player
     * @param player who`s QueueItem to find
     * @return QueueItem in which player or null if player isn`t in queue
     */
    @Nullable
    private QueueItem getPlayerQueueItem(Player player) {
        for (QueueItem queueItem : queue) {
            if (queueItem.getMembers().contains(player)) {
                return queueItem;
            }
        }
        return null;
    }
}