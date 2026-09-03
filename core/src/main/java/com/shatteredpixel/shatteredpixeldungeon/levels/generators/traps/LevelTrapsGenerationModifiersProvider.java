package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;

public class LevelTrapsGenerationModifiersProvider implements TrapsGenerationModifierProvider {

    private final Level level;

    public LevelTrapsGenerationModifiersProvider(Level level) {
        this.level = level;
    }

    @Override
    public Modifier getTrapsGenerationModifier() {
        if (level.feeling == Level.Feeling.TRAPS) return new Modifier(5f, 0f);

        return Modifier.None;
    }
}
