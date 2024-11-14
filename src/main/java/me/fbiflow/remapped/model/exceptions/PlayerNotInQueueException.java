package me.fbiflow.remapped.model.exceptions;

public class PlayerNotInQueueException extends RuntimeException {
    public PlayerNotInQueueException() {
    }

    public PlayerNotInQueueException(String message) {
        super(message);
    }

    public PlayerNotInQueueException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotInQueueException(Throwable cause) {
        super(cause);
    }

    public PlayerNotInQueueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
