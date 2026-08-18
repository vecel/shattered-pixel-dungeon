package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public class DungeonAdapter implements DungeonInterface {

    @Override
    public int getDepth() {
        return Dungeon.depth;
    }

    @Override
    public Trap getTrap(int cell) {
        return Dungeon.level.traps.get(cell);
    }

    @Override
    public void setTrap(Trap trap, int cell) {
        Level level = Dungeon.level;
        Painter.set(level, cell, Terrain.TRAP);
        level.setTrap(trap, cell);
        Level.set(cell, Terrain.TRAP);
        GameScene.updateMap(cell);
    }

    @Override
    public int getCell(int cell) {
        return Dungeon.level.map[cell];
    }

    @Override
    public boolean isCellEmpty(int cell) {
        return Dungeon.level.map[cell] == Terrain.EMPTY;
    }

    @Override
    public boolean isCellMapped(int cell) {
        return Dungeon.level.mapped[cell];
    }

    @Override
    public void setMapped(int cell) {
        Dungeon.level.mapped[cell] = true;
    }

    @Override
    public void discoverCell(int cell) {
        Dungeon.level.discover(cell);
    }

    @Override
    public boolean hasSecretAt(int cell) {
        return Dungeon.level.secret[cell];
    }

    @Override
    public boolean isLevelLocked() {
        return Dungeon.level.locked;
    }


}
