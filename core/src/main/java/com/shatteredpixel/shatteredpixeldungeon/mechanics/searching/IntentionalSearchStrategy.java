package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class IntentionalSearchStrategy extends SearchStrategy {

    private final Shape searchShape;

    public IntentionalSearchStrategy(Shape searchShape) {
        this.searchShape = searchShape;
    }

    @Override
    public void execute(Hero hero) {
        for (int cell : searchShape.getCells(hero.pos)) {
            searchOnCell(hero, cell);
        }

        executePostSearchAction(hero);
    }

    private void searchOnCell(Hero hero, int cell) {
        if (cell == hero.pos) return;
        if (!hero.withinFieldOfView(cell)) return;

        GameScene.effectOverFog(new CheckedCell(cell, hero.pos));

        if (!Dungeon.level.secret[cell]) return;

        int terrain = Dungeon.level.map[cell];
        if (terrain == Terrain.SECRET_TRAP) {
            Trap trap = Dungeon.level.traps.get(cell);
            if (trap == null || !trap.canBeSearched) return;

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

    @Override
    public void executePostSearchAction(Hero hero) {
        hero.sprite.showStatus( CharSprite.DEFAULT, Messages.get(hero, "search") );
        hero.sprite.operate(hero.pos);
        if (!Dungeon.level.locked) {

            if (hero.hasBuff(TalismanOfForesight.Foresight.class)) {
                if (hero.getBuff(TalismanOfForesight.Foresight.class).isCursed()) {
                    GLog.n(Messages.get(hero, "search_distracted"));
                    Buff.affect(hero, Hunger.class).affectHunger(Hero.TIME_TO_SEARCH - (2 * Hero.HUNGER_FOR_SEARCH));
                }
            } else {
                Buff.affect(hero, Hunger.class).affectHunger(Hero.TIME_TO_SEARCH - Hero.HUNGER_FOR_SEARCH);
            }
        }
        hero.spendAndNext(Hero.TIME_TO_SEARCH);

        if (didFoundAnything()) {
            GLog.w( Messages.get(hero, "noticed_smth") );
            Sample.INSTANCE.play( Assets.Sounds.SECRET );
            hero.interrupt();
        }
    }
}
