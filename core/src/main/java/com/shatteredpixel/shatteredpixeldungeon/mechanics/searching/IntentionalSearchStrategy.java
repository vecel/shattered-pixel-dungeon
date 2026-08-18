package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;
import com.watabou.noosa.audio.Sample;

public class IntentionalSearchStrategy extends SearchStrategy {
    private final GameLogger logger;
    private final Audio audio;

    private final Shape searchShape;

    IntentionalSearchStrategy(DungeonInterface dungeon, GameLogger logger, GameSceneInterface scene, Audio audio, Shape searchShape) {
        super(dungeon, scene);
        this.logger = logger;
        this.audio = audio;
        this.searchShape = searchShape;
    }

    public static IntentionalSearchStrategy create(SearchContext context, Shape searchShape) {
        return new IntentionalSearchStrategy(context.getDungeon(), context.getLogger(), context.getScene(), context.getAudio(), searchShape);
    }

    @Override
    public void execute(Hero hero) {
        for (int cell : searchShape.getCells(hero.pos)) {
            searchOnCell(hero, cell);
        }
    }

    @Override
    public void executePostSearchAction(Hero hero) {
        hero.sprite.showStatus(CharSprite.DEFAULT, Messages.get(hero, "search"));
        hero.sprite.operate(hero.pos);
        if (!dungeon.isLevelLocked()) {
            if (hero.hasBuff(TalismanOfForesight.Foresight.class)) {
                if (hero.getBuff(TalismanOfForesight.Foresight.class).isCursed()) {
                    logger.negative(Messages.get(hero, "search_distracted"));
                    hero.affectBuff(Hunger.class).affectHunger(Hero.TIME_TO_SEARCH - (2 * Hero.HUNGER_FOR_SEARCH));
                }
            } else {
                hero.affectBuff(Hunger.class).affectHunger(Hero.TIME_TO_SEARCH - Hero.HUNGER_FOR_SEARCH);
            }
        }
        hero.spendAndNext(Hero.TIME_TO_SEARCH);

        if (didFoundAnything()) {
            logger.warning(Messages.get(hero, "noticed_smth"));
            audio.play(Assets.Sounds.SECRET);
            hero.interrupt();
        }
    }

    private void searchOnCell(Hero hero, int cell) {
        if (cell == hero.pos) return;
        if (!hero.withinFieldOfView(cell)) return;

        scene.effectOverFog(new CheckedCell(cell, hero.pos));

        if (!dungeon.hasSecretAt(cell)) return;

        int terrain = dungeon.getCell(cell);
        if (terrain == Terrain.SECRET_TRAP) {
            Trap trap = dungeon.getTrap(cell);
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
}
