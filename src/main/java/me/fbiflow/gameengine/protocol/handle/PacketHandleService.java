package me.fbiflow.gameengine.protocol.handle;

import me.fbiflow.gameengine.protocol.packet.AbstractPacket;
import me.fbiflow.gameengine.protocol.packet.Packet;
import me.fbiflow.gameengine.util.LoggerUtil;
import me.fbiflow.gameengine.util.SerializeUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.*;

import static java.lang.String.format;

public class PacketHandleService {

    private static PacketHandleService instance;

    private final Map<PacketProducer, List<PacketListener>> listenersMap = Collections.synchronizedMap(new HashMap<>());

    private final LoggerUtil logger = new LoggerUtil(format("| [%s] ->", this.getClass().getSimpleName()));

    private PacketHandleService() {
        start();
    }

    public static PacketHandleService getInstance() {
        if (instance == null) {
            instance = new PacketHandleService();
        }
        return instance;
    }

    private static @Nullable Method[] getMethod(PacketListener listener, AbstractPacket abstractPacket) {
        Method[] declaredMethods = listener.getClass().getDeclaredMethods();
        Method[] handlers = new Method[declaredMethods.length];
        for (Method m : declaredMethods) {
            if (!m.isAnnotationPresent(PacketHandler.class)) {
                continue;
            }
            Parameter[] parameters = m.getParameters();
            if (parameters.length != 3) {
                continue;
            }
            if (parameters[0].getType() != abstractPacket.getClass()
                    || parameters[1].getType() != Packet.class
                    || parameters[2].getType() != Socket.class) {
                continue;
            }
            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] != null) {
                    continue;
                }
                handlers[i] = m;
                break;
            }
        }
        return handlers;
    }

    public void registerListener(PacketProducer packetProducer, PacketListener listener) {
        List<PacketListener> listeners = listenersMap.getOrDefault(packetProducer, new ArrayList<>());
        if (listeners.contains(listener)) {
            throw new RuntimeException("listener is already registered");
        }
        listeners.add(listener);
        listenersMap.put(packetProducer, listeners);
    }

    private void start() {
        Runnable callbackServiceTask = () -> {
            while (true) {
                Map<PacketProducer, List<PacketListener>> snapshot;
                synchronized (listenersMap) {
                    snapshot = new HashMap<>(listenersMap);
                }
                for (Map.Entry<PacketProducer, List<PacketListener>> entry : snapshot.entrySet()) {
                    PacketProducer producer = entry.getKey();
                    List<PacketListener> listeners = entry.getValue();
                    PacketProducer p = producer;
                    List<PacketListener> l = listeners;
                    for (Map<Socket, Packet> packets; (packets = p.pull()) != null; ) {
                        for (Socket socket : packets.keySet()) {
                            Packet packet = packets.get(socket);
                            for (PacketListener listener : l) {
                                handlePacket(packet, socket, listener);
                            }
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(callbackServiceTask);
        thread.start();
    }

    private void handlePacket(Packet source, Socket sender, PacketListener listener) {
        AbstractPacket abstractPacket = null;
        try {
            abstractPacket = (AbstractPacket) SerializeUtil.fromByteArray(source.abstractPacket());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Method[] methods = getMethod(listener, abstractPacket);
        for (Method method : methods) {
            if (method == null) {
            /*logger.log(format("Could not to find handler for packet: %s{%s} in %s{%s}",
                    source.packetClass().getSimpleName(),
                    source.hashCode(),
                    listener.getClass().getSimpleName(),
                    listener.hashCode()));*/
                return;
            }
            method.setAccessible(true);
            try {
                logger.log(format("Handling source: %s{%s} in: %s{%s}...",
                        source.packetClass().getSimpleName(),
                        source.hashCode(),
                        listener.getClass().getSimpleName(),
                        listener.hashCode()));
                method.invoke(listener, abstractPacket, source, sender);

            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(format("An error occurred handling source %s{%s} in: %s{%s}",
                        source.packetClass().getSimpleName(),
                        source.hashCode(),
                        listener.getClass().getSimpleName(),
                        listener.hashCode()));
                throw new RuntimeException(e);
            }
            method.setAccessible(false);
        }
    }
}