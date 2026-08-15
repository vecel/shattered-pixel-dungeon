package com.shatteredpixel.shatteredpixeldungeon.fakes.logger;


import java.util.Objects;

public class LogEntry {
    private final LogLevel level;
    private final String message;

    public LogEntry(LogLevel level, String message) {
        this.level = level;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntry logEntry = (LogEntry) o;
        return level == logEntry.level && Objects.equals(message, logEntry.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, message);
    }
}
