package me.fbiflow;

import me.fbiflow.remapped.model.game.games.Pillars;
import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.packets.AbstractPacket;
import me.fbiflow.remapped.protocol.packet.packets.PlayerQueueJoinRequestPacket;
import me.fbiflow.remapped.protocol.packet.packets.StringPacket;
import me.fbiflow.remapped.util.SerializeUtil;
import me.fbiflow.test.PlayerMock;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class Loader {

    public static void main(String[] arguments) throws IOException, ClassNotFoundException {

        AbstractPacket data = new PlayerQueueJoinRequestPacket(new PlayerMock("FBIFlow"), Pillars.class);
        Packet packet = new Packet(UUID.randomUUID(), UUID.randomUUID(), data.toByteArray(), StringPacket.class);

        byte[] bytes = data.toByteArray();

        System.out.println(SerializeUtil.serialize(UUID.randomUUID()).length);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            String alias = command.split(" ")[0];
            String[] split = command.split(" ");

            String[] args = new String[split.length - 1];
            for (int i = 1; i < split.length; i++) {
                args[i - 1] = split[i];
            }
            switch (alias) {
                case "add-to-queue" : {
                    if (args.length != 1) {
                        System.out.println("incorrect command");
                        break;
                    }
                    //lobbyController.sendPlayerJoin(new PlayerMock(args[0]),
                           // Pillars.class);
                    break;
                }
            }
        }
    }
}