package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

import org.jspecify.annotations.Nullable;

public class HeroProviderAdapter implements HeroProvider {

    @Nullable
    public Hero get() {
        return Dungeon.hero;
    }
}
