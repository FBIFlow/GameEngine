package me.fbiflow.remapped.model.common;

import me.fbiflow.remapped.model.game.AbstractGame;
import me.fbiflow.remapped.model.Party;
import me.fbiflow.remapped.model.QueueItem;
import me.fbiflow.remapped.model.wrapper.internal.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueueManager {

    private final ArrayList<QueueItem> queue;
    private final PartyManager partyManager;

    public QueueManager(PartyManager partyManager) {
        this.queue = new ArrayList<>();
        this.partyManager = partyManager;
    }

    /**
     * Add player to queue, try find existing QueueItem or create new
     *
     * @param player   who joins
     * @param gameType type of required game
     */
    public QueueItem joinQueue(Player player, Class<? extends AbstractGame> gameType) {
        if (!partyManager.isPlayerInParty(player)) {
            QueueItem queueItem = getFreeQueueItem(gameType);
            if (queueItem == null) {
                queueItem = createQueueItem();
                queueItem.setGameType(gameType);
            }
            queueItem.getMembers().add(player);
            //TODO: send message (player joined)
            return queueItem;
        }
        Party party = partyManager.getByMember(player);
        if (party.getOwner() != player) {
            //TODO: send message (only owner can start queue)
            return null;
        }
        Map<String, Integer> maxPartyPlayersMap = GameManager.getMaxPartyPlayers(gameType);
        int allowedPartyPlayers = 0;
        for (String permission : party.getPermissionLevel()) {
            if (maxPartyPlayersMap.get(permission) == null) {
                continue;
            }
            int val = maxPartyPlayersMap.get(permission);
            if (val > allowedPartyPlayers) {
                allowedPartyPlayers = val;
            }
        }
        List<Player> partyMembers = party.getMembersCopy();
        int partyMembersCount = partyMembers.size();
        if (partyMembersCount > GameManager.getMaxPlayers(gameType)) {
            //TODO: send message (too many players to this game)
            return null;
        }
        if (partyMembersCount > allowedPartyPlayers) {
            //TODO: send message (too many players in party)
            return null;
        }
        QueueItem queueItem = getFreeQueueItem(gameType, partyMembersCount);
        if (queueItem == null) {
            queueItem = createQueueItem();
            queueItem.setGameType(gameType);
        }
        for (Player p : partyMembers) {
            queueItem.getMembers().add(p);
            //TODO: send message (player joined)
        }
        return queueItem;
    }

    /**
     * Remove player from queue
     *
     * @param player who leaves
     */
    public void leaveQueue(Player player) {
        QueueItem queueItem = getPlayerQueueItem(player);
        if (queueItem == null) {
            return;
            //TODO: send message (not in queue)
        }
        Party party = partyManager.getByMember(player);
        if (party != null) {
            if (party.getOwner() != player) {
                partyManager.leaveParty(player);
                queueItem.getMembers().remove(player);
                //TODO: send message (leave party and queue)
            } else {
                Player owner = party.getOwner();
                List<Player> partyMembers = party.getMembersCopy();
                partyMembers.remove(owner);

                queueItem.getMembers().remove(owner);
                //TODO: send message (owner leave and all players)
                for (Player p : partyMembers) {
                    queueItem.getMembers().remove(p);
                    //TODO: send message (leave queue because of owner)
                }
            }
        }


        if (queueItem.isEmpty()) {
            removeQueueItemFromQueue(queueItem);
        }
    }

    /**
     * Add QueueItem to queue
     *
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
     *
     * @return created instance of QueueItem
     */
    private QueueItem createQueueItem() {
        QueueItem queueItem = QueueItem.newInstance();
        addQueueItemToQueue(queueItem);
        return queueItem;
    }

    /**
     * Remove QueueItem from queue
     *
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
     *
     * @param gameType type of game
     * @return QueueItem if exists
     */
    @Nullable
    private QueueItem getFreeQueueItem(Class<? extends AbstractGame> gameType) {
        for (QueueItem queueItem : queue) {
            if (!queueItem.isFull() && queueItem.getGameType() == gameType) {
                return queueItem;
            }
        }
        return null;
    }

    /**
     * Find QueueItem with specified game type and existing slots for players
     *
     * @param gameType  type of game
     * @param freeSlots count of free slots
     * @return QueueItem if exists
     */
    @Nullable
    private QueueItem getFreeQueueItem(Class<? extends AbstractGame> gameType, int freeSlots) {
        for (QueueItem queueItem : queue) {
            if (queueItem.getGameType() == gameType && queueItem.getMembers().size() + freeSlots <= queueItem.getMaxPlayers()) {
                return queueItem;
            }
        }
        return null;
    }

    /**
     * Find QueueItem by Player
     *
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