package me.fbiflow.test;

import me.fbiflow.remapped.model.game.games.Pillars;
import me.fbiflow.remapped.model.party.PartyManager;
import me.fbiflow.remapped.model.queue.QueueItem;
import me.fbiflow.remapped.model.queue.QueueManager;

import java.util.List;

public class QueueServiceTest {

    public static void main(String[] args) {
        PartyManager partyManager = new PartyManager();
        QueueManager queueManager = new QueueManager(partyManager);

        List<String> playerNames = List.of(
                "FBIFlow",
                "Meneleet",
                "MoDDuRat",
                "CotoGus",
                "Pivovar",
                "Primer20",
                "Neolit",
                "Atlantice",
                "GindaLineyda",
                "Aside",
                "Phantom_D_E",
                "Striginae"
        );

        List<PlayerMock> players = playerNames.stream().map(PlayerMock::new).toList();

        partyManager.createParty(players.getFirst());

        for (int i = 1; i < 4; i++) {
            partyManager.addInvite(players.getFirst(), players.get(i));
            partyManager.acceptInvite(players.get(i), players.getFirst());
        }

        QueueItem queueItem = queueManager.joinQueue(players.getFirst(), Pillars.class);

        System.out.println(queueItem);;

        for (int i = 5; i < players.size(); i++) {
            queueItem = queueManager.joinQueue(players.get(i), Pillars.class);
            System.out.println(queueItem);;
        }
    }
}