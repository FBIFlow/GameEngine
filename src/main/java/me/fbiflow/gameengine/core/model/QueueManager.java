package me.fbiflow.gameengine.core.model;

import me.fbiflow.gameengine.core.model.game.AbstractGame;
import me.fbiflow.gameengine.core.model.game.GameManager;
import me.fbiflow.gameengine.core.model.wrapper.internal.Player;
import me.fbiflow.gameengine.util.LoggerUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class QueueManager {

    private final LoggerUtil logger = new LoggerUtil("| [QueueManager] ->");

    private final ArrayList<QueueItem> queue;
    private final PartyManager partyManager;

    public QueueManager() {
        this.queue = new ArrayList<>();
        this.partyManager = new PartyManager();
    }

    public PartyManager getPartyManager() {
        return this.partyManager;
    }

    /**
     * Add player to queue, try find existing QueueItem or create new
     *
     * @param player who joins
     * @param gameType type of required game
     */
    public QueueItem joinQueue(Player player, Class<? extends AbstractGame> gameType) {
        if (getPlayerQueueItem(player) != null) {
            //TODO: send message to player
            logger.log(format("Player %s is already in the queue.", player));
            return null;
        }

        if (!partyManager.isPlayerInParty(player)) {
            QueueItem queueItem = getFreeQueueItem(gameType);
            if (queueItem == null) {
                queueItem = createQueueItem();
                queueItem.setGameType(gameType);
            }
            queueItem.addMember(player);
            logger.log(format("Player %s (hash:%s) joined queue %s with game type: %s", player, player.hashCode(), queueItem.hashCode(), gameType.getName()));
            return queueItem;
        }

        Party party = partyManager.getByMember(player);
        if (party.getOwner() != player) {
            //TODO: send message to player
            logger.log("Cannot join queue. Only party owner can perform this.");
            return null;
        }

        Map<String, Integer> maxPartyPlayersMap = GameManager.getMaxPartyPlayers(gameType);
        int allowedPartyPlayers = 0;
        for (String partyPermission : party.getPermissionLevel()) {
            Integer val = maxPartyPlayersMap.get(partyPermission);
            if (val != null && val > allowedPartyPlayers) {
                allowedPartyPlayers = val;
            }
        }

        List<Player> partyMembers = party.getMembers();
        int partyMembersCount = partyMembers.size();
        if (partyMembersCount > GameManager.getMaxPlayers(gameType)) {
            //TODO: send message to player
            logger.log("Too many players in the party for this game.");
            return null;
        }
        if (partyMembersCount > allowedPartyPlayers) {
            //TODO: send message to player
            logger.log("Too many players in the party for the allowed limit.");
            return null;
        }

        QueueItem queueItem = getFreeQueueItem(gameType, partyMembersCount);
        if (queueItem == null) {
            queueItem = createQueueItem();
            queueItem.setGameType(gameType);
        }
        for (Player p : partyMembers) {
            queueItem.addMember(p);
            //TODO: send message to player
        }
        logger.log(format("Player %s (hash:%s) joined queue %s with game type: %s", player, player.hashCode(), queueItem.hashCode(), gameType.getName()));
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
            logger.log(format("Player %s is not in queue",player.getName()));
            return;
            //TODO: send message (not in queue)
        }
        Party party = partyManager.getByMember(player);
        if (party != null) {
            if (party.getOwner() != player) {
                partyManager.leaveParty(player);
                queueItem.removeMember(player);
                logger.log(format("Player %s left from queue and party", player.getName()));
                //TODO: send message (leave party and queue)
            } else {
                Player owner = party.getOwner();
                List<Player> partyMembers = new ArrayList<>(party.getMembers());
                partyMembers.remove(owner);

                queueItem.removeMember(owner);
                logger.log(format("Party owner %s and all players left queue", player.getName()));
                //TODO: send message (owner leave and all players)
                for (Player p : partyMembers) {
                    queueItem.removeMember(p);
                    //TODO: send message (leave queue because of owner)
                }
            }
            return;
        }
        queueItem.removeMember(player);
        logger.log(format("Player %s left queue", player.getName()));
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
        QueueItem queueItem = newQueueItem();
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

    private QueueItem newQueueItem() {
        try {
            Constructor<QueueItem> constructor = QueueItem.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            QueueItem queueItem = constructor.newInstance();
            constructor.setAccessible(false);
            return queueItem;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}