package me.fbiflow.gameengine.core.model.wrapper.internal.event.handle;

import me.fbiflow.gameengine.core.controller.event.EventProducer;

public abstract class AbstractServerEventHandler {

    protected final EventProducer eventProducer;

    public AbstractServerEventHandler(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    public EventProducer getEventProducer() {
        return this.eventProducer;
    }

    public abstract void start();

}
