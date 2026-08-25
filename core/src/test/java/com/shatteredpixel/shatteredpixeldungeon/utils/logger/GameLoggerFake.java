package com.shatteredpixel.shatteredpixeldungeon.utils.logger;

import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

import java.util.ArrayList;
import java.util.List;

public class GameLoggerFake implements GameLogger {

    private final List<LogEntry> logs = new ArrayList<>();

    public List<LogEntry> getLogs() {
        return logs;
    }

    public boolean contains(LogEntry entry) {
        return logs.contains(entry);
    }

    @Override
    public void info(String message) {
        logs.add(new LogEntry(LogLevel.INFO, message));
    }

    @Override
    public void positive(String message) {
        logs.add(new LogEntry(LogLevel.POSITIVE, message));
    }

    @Override
    public void negative(String message) {
        logs.add(new LogEntry(LogLevel.NEGATIVE, message));
    }

    @Override
    public void warning(String message) {
        logs.add(new LogEntry(LogLevel.WARNING, message));
    }

    @Override
    public void highlight(String message) {
        logs.add(new LogEntry(LogLevel.HIGHLIGHT, message));
    }
}