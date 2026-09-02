package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;

public interface DungeonInterface {
    int getDepth();

    Trap getTrap(int cell);

    void setTrap(Trap trap, int cell);

    boolean hasVisibleTrapAt(int cell);

    int getCell(int cell);

    boolean isCellEmpty(int cell);

    boolean isCellGrass(int cell);
    boolean isCellEmbers(int cell);

    boolean isCellOccupied(int cell);

    boolean isCellOccupiedByFlyingCharacter(int cell);

    boolean isCellMapped(int cell);

    void setMapped(int cell);

    void discoverCell(int cell);

    boolean hasSecretAt(int cell);

    boolean isLevelLocked();
}
