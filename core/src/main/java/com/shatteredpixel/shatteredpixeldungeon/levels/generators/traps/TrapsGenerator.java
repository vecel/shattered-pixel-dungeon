package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TrapsGenerator {
    int getCount(@NotNull Level level, @NotNull TrapsGenerationModifierProvider provider);
    @NotNull
    TrapsPool getPool(@NotNull Level level);
}
