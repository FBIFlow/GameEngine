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

public class CallbackService {

    private static CallbackService instance;

    private final Map<PacketProducer, List<PacketListener>> listenersMap = Collections.synchronizedMap(new HashMap<>());

    private final LoggerUtil logger = new LoggerUtil(format("| [%s] ->", this.getClass().getSimpleName()));

    private CallbackService() {
    }

    public static CallbackService getInstance() {
        if (instance == null) {
            instance = new CallbackService();
        }
        return instance;
    }

    private static @Nullable Method getMethod(PacketListener listener, AbstractPacket abstractPacket) {
        Method[] methods = listener.getClass().getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
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
            method = m;
        }
        return method;
    }

    public void registerListener(PacketProducer packetProducer, PacketListener listener) {
        List<PacketListener> listeners = listenersMap.getOrDefault(packetProducer, new ArrayList<>());
        if (listeners.contains(listener)) {
            throw new RuntimeException("listener is already registered");
        }
        listeners.add(listener);
        listenersMap.put(packetProducer, listeners);
    }

    public void start() {
        Runnable callbackServiceTask = () -> {
            while (true) {
                listenersMap.forEach((producer, listeners) -> {
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
                });
            }
        };
        Thread thread = new Thread(callbackServiceTask);
        thread.start();
    }

    private void handlePacket(Packet source, Socket sender, PacketListener listener) {
        //TODO: reflection api to throw all received packets from producer to listeners
        AbstractPacket abstractPacket = null;
        try {
            abstractPacket = (AbstractPacket) SerializeUtil.deserialize(source.abstractPacket());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Method method = getMethod(listener, abstractPacket);
        if (method == null) {
            logger.log(format("Could not to find handler for packet: %s{%s} in %s{%s}",
                    source.packetClass().getSimpleName(),
                    source.hashCode(),
                    listener.getClass().getSimpleName(),
                    listener.hashCode()));
            return;
        }
        logger.log(format("Handled source: %s{%s} in: %s{%s}",
                source.packetClass().getSimpleName(),
                source.hashCode(),
                listener.getClass().getSimpleName(),
                listener.hashCode()));
        method.setAccessible(true);
        try {
            method.invoke(listener, abstractPacket, source, sender);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(false);
    }
}