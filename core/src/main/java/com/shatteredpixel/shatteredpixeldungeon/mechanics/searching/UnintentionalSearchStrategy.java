package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class UnintentionalSearchStrategy extends SearchStrategy {

    private final Shape trapSearchShape;
    private final Shape doorSearchShape;

    public UnintentionalSearchStrategy(Shape trapSearchShape, Shape doorSearchShape) {
        this.trapSearchShape = trapSearchShape;
        this.doorSearchShape = doorSearchShape;
    }

    @Override
    public void execute(Hero hero) {
        for (int cell : trapSearchShape.getCells(hero.pos)) {
            if (shouldSkipSearching(hero, cell)) continue;
            searchTrapOnCell(hero, cell);
        }

        for (int cell : doorSearchShape.getCells(hero.pos)) {
            if (shouldSkipSearching(hero, cell)) continue;
            searchDoorOnCell(hero, cell);
        }

        executePostSearchAction(hero);
    }

    @Override
    public void executePostSearchAction(Hero hero) {
        if (didFoundAnything()) {
            GLog.w( Messages.get(hero, "noticed_smth") );
            Sample.INSTANCE.play( Assets.Sounds.SECRET );
            hero.interrupt();
        }
    }

    private void searchTrapOnCell(Hero hero, int cell) {
        if (Dungeon.level.map[cell] == Terrain.SECRET_TRAP) {
            Trap trap = Dungeon.level.traps.get(cell);
            if (trap == null || !trap.canBeSearched) return;
            float chance = 0.4f - (Dungeon.depth / 250f);
            if (hero.hasTalent(Talent.TRAP_SENSE)) chance *= 2;

            if (Random.Float() < chance) {
                discover(cell);
                hero.chargeTalismanIfPresent(2);
                trapsFound += 1;
            }
        }
    }

    private void searchDoorOnCell(Hero hero, int cell) {
        if (Dungeon.level.map[cell] == Terrain.SECRET_DOOR) {
            float chance = 0.2f - (Dungeon.depth / 100f);
            if (Random.Float() < chance) {
                discover(cell);
                hero.chargeTalismanIfPresent(10);
                doorFound += 1;
            }
        }
    }

    private boolean shouldSkipSearching(Hero hero, int cell) {
        if (cell == hero.pos) return true;
        if (!hero.withinFieldOfView(cell)) return true;
        return !Dungeon.level.secret[cell];
    }
}
