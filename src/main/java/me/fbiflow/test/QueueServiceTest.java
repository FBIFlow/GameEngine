package me.fbiflow.test;

import me.fbiflow.gameengine.model.game.games.Pillars;
import me.fbiflow.gameengine.model.QueueUnit;
import me.fbiflow.gameengine.service.consumer.ConsumerService;
import me.fbiflow.gameengine.service.transfer.DataReceiver;
import me.fbiflow.gameengine.service.transfer.DataSender;
import me.fbiflow.gameengine.service.transfer.impl.InternalDataReceiver;
import me.fbiflow.gameengine.service.transfer.impl.InternalDataSender;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class QueueServiceTest {


    public static void main(String[] args) {
        DataReceiver dataReceiver = new InternalDataReceiver();
        DataSender dataSender = new InternalDataSender(dataReceiver);

        ConsumerService consumerService = new ConsumerService(null, null, null);
        Scanner scanner = new Scanner(System.in);
        String[] input;
        do {
            input = scanner.nextLine().split(" ");
            switch (input[0]) {
                case "add-player" -> consumerService.addToQueue(new PlayerMock(input[1]), Pillars.class);
                case "isin-queue" -> {
                    long currentTime = System.currentTimeMillis();
                    System.out.println(consumerService.isPlayerInQueue(PlayerMock.getPlayer(input[1])));
                    System.out.printf("Time spent: %s\n", (System.currentTimeMillis() - currentTime));
                }
                case "queue-print" -> {
                    List<QueueUnit> que = consumerService.getQueue();
                    for (QueueUnit unit : que) {
                        System.out.println(unit);
                    }
                    break;
                }
                case "queue-count" -> System.out.println(consumerService.getQueue().size());
                case "queue-user-count" -> System.out.println(consumerService.getUsersInQueue());
            }
        } while (!input[0].equals("stop"));
    }


}
