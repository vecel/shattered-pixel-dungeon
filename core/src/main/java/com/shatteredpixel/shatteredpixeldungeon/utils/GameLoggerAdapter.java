package com.shatteredpixel.shatteredpixeldungeon.utils;

public class GameLoggerAdapter implements GameLogger {

    @Override
    public void info(String message) {
        GLog.i(message);
    }

    @Override
    public void positive(String message) {
        GLog.p(message);
    }

    @Override
    public void negative(String message) {
        GLog.n(message);
    }

    @Override
    public void warning(String message) {
        GLog.w(message);
    }

    @Override
    public void highlight(String message) {
        GLog.h(message);
    }
}
