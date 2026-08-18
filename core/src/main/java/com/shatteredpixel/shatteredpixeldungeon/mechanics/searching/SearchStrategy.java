package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

public abstract class SearchStrategy {
    protected final DungeonInterface dungeon;
    protected final GameSceneInterface scene;

    protected int trapsFound = 0;
    protected int doorFound = 0;

    protected SearchStrategy(DungeonInterface dungeon, GameSceneInterface scene) {
        this.dungeon = dungeon;
        this.scene = scene;
    }

    public abstract void execute(Hero hero);

    public abstract void executePostSearchAction(Hero hero);

    public boolean didFoundAnything() {
        return trapsFound > 0 || doorFound > 0;
    }

    protected void discover(int cell) {
        int oldValue = dungeon.getCell(cell);

        scene.discover(cell, oldValue);
        dungeon.discoverCell(cell);
        scene.discoverWithScrollOfMagicMapping(cell);
    }
}
