package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;

public interface DungeonInterface {

    Trap getTrap(int cell);

    void setTrap(Trap trap, int cell);

    boolean isCellEmpty(int cell);
}
