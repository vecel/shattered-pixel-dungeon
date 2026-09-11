package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.effects.HighlightedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.noosa.Visual;

import java.util.List;

public class GameSceneAdapter implements GameSceneInterface {
    @Override
    public void updateInventory() {
        GameScene.updateItemDisplays = true;
    }

    @Override
    public void updateFog(int cell, int radius) {
        GameScene.updateFog(cell, radius);
    }

    @Override
    public void effectOverFog(Visual effect) {
        GameScene.effectOverFog(effect);
    }

    @Override
    public void discover(int cell, int oldValue) {
        GameScene.discoverTile(cell, oldValue);
    }

    @Override
    public void discoverWithScrollOfMagicMapping(int cell) {
        ScrollOfMagicMapping.discover(cell);
    }

    @Override
    public void highlight(List<Integer> cells) {
        cells.forEach(cell -> GameScene.highlight(new HighlightedCell(cell)));
    }

    @Override
    public void cancelHighlight() {
        GameScene.removeHighlight();
        GameScene.updateMap();
    }
}
