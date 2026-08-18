package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.watabou.noosa.Visual;

public interface GameSceneInterface {
    void updateFog(int cell, int radius);
    void effectOverFog(Visual effect);
    void discover(int cell, int oldValue);

    void discoverWithScrollOfMagicMapping(int cell);
}
