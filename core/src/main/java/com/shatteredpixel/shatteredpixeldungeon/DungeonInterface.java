package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;

public interface DungeonInterface {

    Trap getTrap(int cell);

    void setTrap(Trap trap, int cell);

    int getCell(int cell);

    boolean isCellEmpty(int cell);

    boolean isCellMapped(int cell);

    void setMapped(int cell);

    void discoverCell(int cell);

    boolean hasSecretAt(int cell);

    boolean isLevelLocked();
}
