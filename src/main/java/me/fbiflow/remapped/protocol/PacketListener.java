package me.fbiflow.remapped.protocol;

import me.fbiflow.remapped.protocol.packet.Packet;
import me.fbiflow.remapped.protocol.packet.AbstractPacket;
import me.fbiflow.remapped.util.LoggerUtil;
import me.fbiflow.remapped.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public interface PacketListener {

    LoggerUtil getLogger();

    default void handlePacket(Packet source, Socket sender) {
        LoggerUtil logger = getLogger();
        try {
            AbstractPacket abstractPacket = (AbstractPacket) SerializeUtil.deserialize(source.data());
            Method method = null;
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (!m.isAnnotationPresent(PacketHandler.class)) {
                    continue;
                }
                Class<?> paramType = m.getParameters()[0].getType();
                if (paramType != abstractPacket.getClass()) {
                    continue;
                }
                method = m;
            }
            if (method == null) {
                logger.log(format("Could not to find handler for packet type: %s. null.", source.packetClass().getName()));
                return;
            }
            logger.log(format("Handled packet: %s with handler: %s", source.packetClass().getSimpleName(), method.getParameters()[0].getType().getSimpleName()));
            method.setAccessible(true);
            method.invoke(this, abstractPacket, source, sender);
            method.setAccessible(false);
        } catch (IOException | NullPointerException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.log(format("Could not to find handler for packet type: %s", source.packetClass().getName()));
        }
    }

    default void startListener(PacketHolder packetHolder) {
        Thread thread = new Thread(() -> {
            while (true) {
                Map<Socket, List<Packet>> packets = packetHolder.getReceivedPackets();
                for (Socket sender : packets.keySet()) {
                    List<Packet> packetlist = packets.get(sender);
                    if (packetlist.isEmpty()) {
                        continue;
                    }
                    handlePacket(packetlist.removeFirst(), sender);
                }
            }
        });
        thread.start();
    }
}
