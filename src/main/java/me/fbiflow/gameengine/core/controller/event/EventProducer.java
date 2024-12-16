package me.fbiflow.gameengine.core.controller.event;

import me.fbiflow.gameengine.core.model.wrapper.internal.event.AbstractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventProducer {

    private static final Map<Object, EventProducer> producers = new HashMap<>();

    private final List<AbstractEvent> abstractEvents = new ArrayList<>();

    public static EventProducer of(Object link) {
        if (producers.containsKey(link)) {
            return producers.get(link);
        }
        EventProducer producer = new EventProducer();
        producers.put(link, producer);
        return producer;
    }

    public void produce(AbstractEvent abstractEvent) {
        System.err.println("produced new event");
        if (abstractEvents.contains(abstractEvent)) {
            throw new RuntimeException("event already exists");
        }
        abstractEvents.add(abstractEvent);
    }

    @Nullable
    public AbstractEvent pull() {
        if (abstractEvents.isEmpty()) {
            return null;
        }
        return abstractEvents.remove(0);
    }
}