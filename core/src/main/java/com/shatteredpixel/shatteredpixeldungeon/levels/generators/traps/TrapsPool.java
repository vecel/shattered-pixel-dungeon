package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import com.google.common.primitives.Floats;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapType;
import com.watabou.utils.Random;

import java.util.List;

public class TrapsPool {
    private final List<TrapType> types;
    private final List<Float> chances;

    public static final TrapsPool Empty = new TrapsPool(List.of(), List.of());

    public TrapsPool(List<TrapType> types, List<Float> chances) {
        this.types = types;
        this.chances = chances;
    }

    public Trap createTrap() {
        TrapType type = types.get(Random.chances(Floats.toArray(chances)));
        return type.create();
    }
}
