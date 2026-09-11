package com.shatteredpixel.shatteredpixeldungeon.events;

public interface Listener<T extends GameEvent> {
    void onEvent(T event);
}
