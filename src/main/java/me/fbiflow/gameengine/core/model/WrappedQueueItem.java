package me.fbiflow.gameengine.core.model;

import me.fbiflow.gameengine.util.LoggerUtil;
import org.jetbrains.annotations.Nullable;

public class WrappedQueueItem {

    public final QueueItem queueItem;
    public final String reason;

    public WrappedQueueItem(QueueItem queueItem) {
        this.queueItem = queueItem;
        this.reason = null;
    }

    public WrappedQueueItem(String reason) {
        this.queueItem = null;
        this.reason = reason;
    }
}
