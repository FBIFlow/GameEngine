package me.fbiflow.remapped.util;

import java.time.LocalTime;

public class LoggerUtil {

    private String prefix;

    public LoggerUtil(String prefix) {
        this.prefix = prefix;
    }

    public void log(String text) {
        System.out.println(LocalTime.now() + prefix + text);
    }

}
