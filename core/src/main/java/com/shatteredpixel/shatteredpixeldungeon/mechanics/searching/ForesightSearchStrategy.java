package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;
import com.watabou.noosa.audio.Sample;

public class ForesightSearchStrategy extends SearchStrategy {
    private final Shape searchShape;

    ForesightSearchStrategy(DungeonInterface dungeon, GameSceneInterface scene, Shape searchShape) {
        super(dungeon, scene);
        this.searchShape = searchShape;
    }

    public static ForesightSearchStrategy create(SearchContext context, Shape searchShape) {
        return new ForesightSearchStrategy(context.getDungeon(), context.getScene(), searchShape);
    }

    @Override
    public void execute(Hero hero) {
        for (int cell : searchShape.getCells(hero.pos)) {
            searchOnCell(hero, cell);
        }
    }

    @Override
    public void executePostSearchAction(Hero hero) {
        scene.updateFog(hero.pos, Foresight.DISTANCE+1);
    }

    private void searchOnCell(Hero hero, int cell) {
        int position = hero.getPosition();
        if (cell == position) return;

        if (!dungeon.isCellMapped(cell)) {
            scene.effectOverFog(new CheckedCell(cell, position));
        }

        dungeon.setMapped(cell);

        if (!dungeon.hasSecretAt(cell)) return;

        int terrain = dungeon.getCell(cell);

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
