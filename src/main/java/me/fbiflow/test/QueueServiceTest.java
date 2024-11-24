package me.fbiflow.test;

import me.fbiflow.gameengine.core.model.PartyManager;
import me.fbiflow.gameengine.core.model.QueueManager;

import java.util.List;

public class QueueServiceTest {

    public static void main(String[] args) {
        PartyManager partyManager = new PartyManager();
        QueueManager queueManager = new QueueManager();

        List<String> playerNames = List.of(
                "FBIFlow",
                "Meneleet",
                "MoDDuRat",
                "CotoGus",
                "Pivovar",
                "Primer2.0",
                "Neolit",
                "Atlantice",
                "GindaLineyda",
                "Aside",
                "Phantom_D_E",
                "Striginae"
        );
    }
}