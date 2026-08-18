package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.watabou.noosa.Visual;

public class GameSceneAdapter implements GameSceneInterface {
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
}
