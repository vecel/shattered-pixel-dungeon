package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.watabou.noosa.Visual;

import java.util.List;

public interface GameSceneInterface {
    void updateFog(int cell, int radius);
    void effectOverFog(Visual effect);
    void discover(int cell, int oldValue);
    void discoverWithScrollOfMagicMapping(int cell);
    void highlight(List<Integer> cells);
    void cancelHighlight();
}
