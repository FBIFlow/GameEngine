package me.fbiflow.gameengine.service.producer;

import me.fbiflow.gameengine.model.transfer.packet.Packet;
import me.fbiflow.gameengine.model.transfer.packet.PacketHandler;
import me.fbiflow.gameengine.model.transfer.packet.PacketType;
import me.fbiflow.gameengine.service.transfer.DataReceiver;
import me.fbiflow.gameengine.service.transfer.DataSender;
import org.bukkit.event.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.String.format;

public class ProducerService {

    private final DataSender consumerSender;
    private final DataReceiver consumerReceiver;

    public ProducerService(DataSender consumerSender, DataReceiver consumerReceiver) {
        this.consumerSender = consumerSender;
        this.consumerReceiver = consumerReceiver;
    }

    public void addToQueue() {

    }


    /*@PacketHandler(packetType = PacketType.QUEUE_UNIT_UPDATE)
    private void handlePacket(Packet packet) {

    }

    public void startDataReceive() {
        Thread dataReceiveThread = new Thread(() -> {
            while(true) {
                Packet packet = consumerReceiver.getData();
                if (packet == null) {
                    continue;
                }
                try {
                    Method[] methods = ProducerService.class.getMethods();
                    for (Method method : methods) {
                        if (!method.isAnnotationPresent(PacketHandler.class)) {
                            continue;
                        }
                        if (method.getAnnotation(PacketHandler.class).packetType() == packet.packetType) {
                            method.invoke(this,packet);
                        }
                    }
                    throw new RuntimeException(format("received packet %s, but haven`t handler for this packet type\n", packet.packetType));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dataReceiveThread.start();
    }*/
}