package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;

public class FailedUnintentionalSearchStrategy extends SearchStrategy {

    FailedUnintentionalSearchStrategy(DungeonInterface dungeon, GameSceneInterface scene) {
        super(dungeon, scene);
    }

    public static FailedUnintentionalSearchStrategy create(SearchContext context) {
        return new FailedUnintentionalSearchStrategy(context.getDungeon(), context.getScene());
    }

    @Override
    public void execute(Hero hero) {

    }

    @Override
    public void executePostSearchAction(Hero hero) {
        
    }
}
