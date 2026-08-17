package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ForesightSearchStrategy extends SearchStrategy {
    private final Shape searchShape;

    public ForesightSearchStrategy(Shape searchShape) {
        this.searchShape = searchShape;
    }

    @Override
    public void execute(Hero hero) {
        for (int cell : searchShape.getCells(hero.pos)) {
            searchOnCell(hero, cell);
        }

        executePostSearchAction(hero);
    }

    @Override
    public void executePostSearchAction(Hero hero) {
        GameScene.updateFog(hero.pos, Foresight.DISTANCE+1);
        if (hero.hasBuff(TalismanOfForesight.Foresight.class)) {
            hero.getBuff(TalismanOfForesight.Foresight.class).checkAwareness();
        }
    }

    private void searchOnCell(Hero hero, int cell) {
        if (cell == hero.pos) return;

        if (!Dungeon.level.mapped[cell]) {
            GameScene.effectOverFog(new CheckedCell(cell, hero.pos));
        }

        Dungeon.level.mapped[cell] = true;

        if (!Dungeon.level.secret[cell]) return;

        int terrain = Dungeon.level.map[cell];

        if (terrain == Terrain.SECRET_TRAP) {
            discover(cell);
            hero.chargeTalismanIfPresent(2);
            trapsFound += 1;
        }

        if (terrain == Terrain.SECRET_DOOR) {
            discover(cell);
            hero.chargeTalismanIfPresent(10);
            doorFound += 1;
        }
    }
}
