package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public class DungeonAdapter implements DungeonInterface {

    @Override
    public void setLevel(Level level) {
        Dungeon.level = level;
    }

    @Override
    public Level getLevel() {
        return Dungeon.level;
    }

    @Override
    public Trap getTrap(int cell) {
        return getLevel().traps.get(cell);
    }

    @Override
    public void setTrap(Trap trap, int cell) {
        Level level = getLevel();
        Painter.set(level, cell, Terrain.TRAP);
        level.setTrap(trap, cell);
        GameScene.updateMap(cell);
    }

    @Override
    public boolean isCellEmpty(int cell) {
        return getLevel().map[cell] == Terrain.EMPTY;
    }


}
