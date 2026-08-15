package com.shatteredpixel.shatteredpixeldungeon.utils;

public interface GameLogger {
    void info(String message);
    void positive(String message);
    void negative(String message);
    void warning(String message);
    void highlight(String message);
}
