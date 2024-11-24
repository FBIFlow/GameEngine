package me.fbiflow.gameengine.protocol;

import me.fbiflow.gameengine.protocol.packet.AbstractPacket;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.util.LoggerUtil;
import me.fbiflow.gameengine.util.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public abstract class PacketListener {


    private PacketHolder server;

    private LoggerUtil logger = new LoggerUtil(format(" | [%s] ->", this.getClass().getName()));

    void handlePacket(Packet source, Socket sender) {
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

    protected void startListener(PacketHolder packetHolder) {
        if (this.server != null) {
            throw new IllegalStateException("Can`t start service. Already started");
        }
        this.server = packetHolder;
        Thread thread = new Thread(() -> {
            while (true) {
                Map<Socket, List<Packet>> packets = server.getReceivedPackets();
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
