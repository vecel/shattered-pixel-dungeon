package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class UnintentionalSearchStrategy extends SearchStrategy {
    private final GameLogger logger;
    private final Audio audio;
    private final Shape trapSearchShape;
    private final Shape doorSearchShape;

    UnintentionalSearchStrategy(DungeonInterface dungeon, GameLogger logger, GameSceneInterface scene, Audio audio, Shape trapSearchShape, Shape doorSearchShape) {
        super(dungeon, scene);
        this.logger = logger;
        this.audio = audio;
        this.trapSearchShape = trapSearchShape;
        this.doorSearchShape = doorSearchShape;
    }

    public static UnintentionalSearchStrategy create(SearchContext context, Shape trapSearchShape, Shape doorSearchShape) {
        return new UnintentionalSearchStrategy(context.getDungeon(), context.getLogger(), context.getScene(), context.getAudio(), trapSearchShape, doorSearchShape);
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
    }

    @Override
    public void executePostSearchAction(Hero hero) {
        if (didFoundAnything()) {
            logger.warning(Messages.get(hero, "noticed_smth"));
            audio.play(Assets.Sounds.SECRET);
            hero.interrupt();
        }
    }

    private void searchTrapOnCell(Hero hero, int cell) {
        if (dungeon.getCell(cell) == Terrain.SECRET_TRAP) {
            Trap trap = dungeon.getTrap(cell);
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
        if (dungeon.getCell(cell) == Terrain.SECRET_DOOR) {
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
        return !dungeon.hasSecretAt(cell);
    }
}
