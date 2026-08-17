package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public abstract class SearchStrategy {

    protected int trapsFound = 0;
    protected int doorFound = 0;

    public abstract void execute(Hero hero);

    public abstract void executePostSearchAction(Hero hero);

    public boolean didFoundAnything() {
        return trapsFound > 0 || doorFound > 0;
    }

    protected void discover(int cell) {
        int oldValue = Dungeon.level.map[cell];

        GameScene.discoverTile(cell, oldValue);
        Dungeon.level.discover(cell);
        ScrollOfMagicMapping.discover(cell);
    }
}
