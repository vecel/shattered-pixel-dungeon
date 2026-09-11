package com.shatteredpixel.shatteredpixeldungeon.events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListenersRegistry {

    private final Map<Class<? extends GameEvent>, List<Listener<?>>> listeners = new HashMap<>();

    public <T extends GameEvent> void add(Class<T> type, Listener<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public <T extends GameEvent> void remove(Listener<T> listener) {
        for (List<Listener<?>> targets : listeners.values()) {
            targets.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void notify(T event) {
        Class<? extends GameEvent> type = event.getClass();
        List<Listener<?>> targets = listeners.get(type);
        if (targets == null) return;
        for (Listener<?> target : targets) {
            ((Listener<T>) target).onEvent(event);
        }
    }
}
