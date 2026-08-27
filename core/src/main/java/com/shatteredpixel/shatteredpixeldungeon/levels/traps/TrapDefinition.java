package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import java.util.function.Supplier;

public class TrapDefinition {

    private final int danger;
    private final Supplier<Trap> factory;

    public TrapDefinition(Supplier<Trap> factory, int danger) {
        this.danger = danger;
        this.factory = factory;
    }

    public int getDanger() {
        return danger;
    }

    public Supplier<Trap> getFactory() {
        return factory;
    }
}
