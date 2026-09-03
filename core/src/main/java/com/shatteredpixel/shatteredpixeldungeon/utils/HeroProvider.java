package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

import org.jetbrains.annotations.Nullable;

public interface HeroProvider {

    @Nullable Hero get();
}
