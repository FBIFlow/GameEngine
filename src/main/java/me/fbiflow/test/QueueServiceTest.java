package me.fbiflow.test;

import me.fbiflow.remapped.model.party.PartyManager;
import me.fbiflow.remapped.model.queue.QueueManager;

public class QueueServiceTest {

    public static void main(String[] args) {
        PartyManager partyManager = new PartyManager();
        QueueManager queueManager = new QueueManager(partyManager);

    }

}
