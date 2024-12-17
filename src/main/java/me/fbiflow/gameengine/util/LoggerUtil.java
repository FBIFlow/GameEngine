package me.fbiflow.gameengine.util;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Arrays;

import static java.lang.String.format;

public class LoggerUtil {

    private String prefix;

    public LoggerUtil(String prefix) {
        this.prefix = prefix;
    }

    public void log(String text) {
        LocalDateTime t = LocalDateTime.now();
        String time = format("[%s.%s.%s] %s:%s:%s:%s",
                t.getYear(), t.getMonthValue(), t.getDayOfMonth(), t.getHour(), t.getMinute(), t.getSecond(), t.getNano());
        System.out.printf("%s %s %s%n", time, prefix, text);
    }

    public void error(String text) {
        LocalDateTime t = LocalDateTime.now();
        String time = format("[%s.%s.%s] %s:%s:%s",
                t.getYear(), t.getMonthValue(), t.getDayOfMonth(), t.getHour(), t.getMinute(), t.getSecond());
        System.out.printf("[ERROR] %s %s %s%n", time, prefix, text);
    }
}