package me.fbiflow.gameengine.core.controller.event;

import me.fbiflow.gameengine.core.model.wrapper.internal.event.AbstractEvent;
import me.fbiflow.gameengine.util.LoggerUtil;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.lang.String.format;

public class EventHandleService {

    private static EventHandleService instance;

    private final Map<EventProducer, List<EventListener>> listenersMap = Collections.synchronizedMap(new HashMap<>());

    private final LoggerUtil logger = new LoggerUtil(format("| [%s] ->", this.getClass().getSimpleName()));

    private EventHandleService() {
        start();
    }

    public static EventHandleService getInstance() {
        if (instance == null) {
            instance = new EventHandleService();
        }
        return instance;
    }

    private static @Nullable Method[] getMethod(EventListener listener, AbstractEvent abstractEvent) {
        Method[] declaredMethods = listener.getClass().getDeclaredMethods();
        Method[] handlers = new Method[declaredMethods.length];
        for (Method m : declaredMethods) {
            if (!m.isAnnotationPresent(EventHandler.class)) {
                continue;
            }
            Parameter[] parameters = m.getParameters();
            if (parameters.length != 1) {
                continue;
            }
            if (parameters[0].getType() != abstractEvent.getClass()) {
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

    public void registerListener(EventProducer eventProducer, EventListener listener) {
        List<EventListener> listeners = listenersMap.getOrDefault(eventProducer, new ArrayList<>());
        if (listeners.contains(listener)) {
            throw new RuntimeException("listener is already registered");
        }
        listeners.add(listener);
        listenersMap.put(eventProducer, listeners);
    }

    private void start() {
        Runnable callbackServiceTask = () -> {
            while (true) {
                Map<EventProducer, List<EventListener>> snapshot;
                synchronized (listenersMap) {
                    snapshot = new HashMap<>(listenersMap);
                }
                for (Map.Entry<EventProducer, List<EventListener>> entry : snapshot.entrySet()) {
                    EventProducer producer = entry.getKey();
                    List<EventListener> listeners = entry.getValue();
                    EventProducer p = producer;
                    List<EventListener> l = listeners;
                    for (AbstractEvent event; (event = p.pull()) != null; ) {
                        for (EventListener listener : l) {
                            handleEvent(event, listener);
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(callbackServiceTask);
        thread.start();
    }

    private void handleEvent(AbstractEvent abstractEvent, EventListener listener) {

        Method[] methods = getMethod(listener, abstractEvent);
        for (Method method : methods) {
            if (method == null) {
            /*logger.log(format("Could not to find handler for event: %s{%s} in %s{%s}",
                    abstractEvent.packetClass().getSimpleName(),
                    abstractEvent.hashCode(),
                    listener.getClass().getSimpleName(),
                    listener.hashCode()));*/
                return;
            }
            method.setAccessible(true);
            try {
                logger.log(format("Handling event: %s{%s} in: %s{%s}...",
                        abstractEvent.getClass().getSimpleName(),
                        abstractEvent.hashCode(),
                        listener.getClass().getSimpleName(),
                        listener.hashCode()));
                method.invoke(listener, abstractEvent);

            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(format("An error occurred handling abstractEvent %s{%s} in: %s{%s}",
                        abstractEvent.getClass().getSimpleName(),
                        abstractEvent.hashCode(),
                        listener.getClass().getSimpleName(),
                        listener.hashCode()));
                throw new RuntimeException(e);
            } finally {
                method.setAccessible(false);
            }
        }
    }
}