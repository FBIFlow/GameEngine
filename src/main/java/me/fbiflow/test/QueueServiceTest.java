package me.fbiflow.test;

import me.fbiflow.remapped.model.game.games.Pillars;
import me.fbiflow.remapped.model.party.PartyManager;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.model.queue.QueueManager;
import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.List;
import java.util.Set;

public class QueueServiceTest {

    public static void main(String[] args) {
        PartyManager partyManager = new PartyManager();
        QueueManager queueManager = new QueueManager(partyManager);

        Set<String> playerNames = Set.of(
                "FBIFlow",
                "Meneleet",
                "MoDDuRat",
                "CotoGus"/*,
                "Pivovar",
                "Primer20",
                "Neolit",
                "Atlantice"*/
        );

        List<PlayerMock> players = playerNames.stream().map(PlayerMock::new).toList();

        partyManager.createParty(players.getFirst());

        for (int i = 1; i < players.size(); i++) {
            partyManager.addInvite(players.getFirst(), players.get(i));
            partyManager.acceptInvite(players.get(i), players.getFirst());
        }

        QueueItem queueItem = queueManager.joinQueue(players.getFirst(), Pillars.class);

        System.out.println(queueItem.toString());;
    }

}
