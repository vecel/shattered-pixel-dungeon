package com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion;

import org.jetbrains.annotations.NotNull;

public interface ExplosionStrategy {

    @NotNull
    ExplosionResult calculateExplosionArea(int cell);
}
