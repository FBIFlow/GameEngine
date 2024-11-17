package me.fbiflow.test;

import me.fbiflow.remapped.model.common.PartyManager;
import me.fbiflow.remapped.model.common.QueueManager;

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