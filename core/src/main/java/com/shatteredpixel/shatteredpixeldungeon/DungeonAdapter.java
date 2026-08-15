package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public class DungeonAdapter implements DungeonInterface {
    @Override
    public Trap getTrap(int cell) {
        return Dungeon.level.traps.get(cell);
    }

    @Override
    public void setTrap(Trap trap, int cell) {
        Painter.set(Dungeon.level, cell, Terrain.TRAP);
        Dungeon.level.setTrap(trap, cell);
        Level.set(cell, Terrain.TRAP);
        GameScene.updateMap(cell);
    }

    @Override
    public boolean isCellEmpty(int cell) {
        return Dungeon.level.map[cell] == Terrain.EMPTY;
    }


}
