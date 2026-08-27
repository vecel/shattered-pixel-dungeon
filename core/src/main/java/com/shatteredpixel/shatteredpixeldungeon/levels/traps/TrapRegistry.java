package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

public interface TrapRegistry {

    int getDanger(Class<? extends  Trap> trapClass);

    Trap create(Class<? extends  Trap> trapClass);
}
