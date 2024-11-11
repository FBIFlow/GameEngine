package me.fbiflow.test;

import me.fbiflow.gameengine.model.game.games.Pillars;
import me.fbiflow.gameengine.model.queue.QueueUnit;
import me.fbiflow.gameengine.service.QueueService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

public class QueueServiceTest {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        QueueService queueService = new QueueService();
        Scanner scanner = new Scanner(System.in);
        String[] input;
        do {
            input = scanner.nextLine().split(" ");
            switch (input[0]) {
                case "add-player" -> queueService.addToQueue(new PlayerMock(input[1]), Pillars.class);
                case "isin-queue" -> System.out.println(queueService.isPlayerInQueue(PlayerMock.getPlayer(input[1])));
                case "queue-print" -> {
                    List<QueueUnit> que = queueService.getQueue();
                    for (QueueUnit unit : que) {
                        System.out.println(unit);
                    }
                    break;
                }
                case "queue-count" -> System.out.println(queueService.getQueue().size());
                case "queue-user-count" -> System.out.println(queueService.getUsersInQueue());
            }
        } while (!input[0].equals("stop"));
    }


}
