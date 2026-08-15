package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;

public interface DungeonInterface {

    void setLevel(Level level);

    Level getLevel();

    Trap getTrap(int cell);

    void setTrap(Trap trap, int cell);

    boolean isCellEmpty(int cell);
}
