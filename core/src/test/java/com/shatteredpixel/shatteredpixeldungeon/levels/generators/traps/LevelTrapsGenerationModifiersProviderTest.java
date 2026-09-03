package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LevelTrapsGenerationModifiersProviderTest {

    private LevelTrapsGenerationModifiersProvider provider;
    private Level level;

    @BeforeEach
    void setUp() {
        level = mock(Level.class);

        provider = new LevelTrapsGenerationModifiersProvider(level);
    }

    @Test
    void provides_default_modifier_when_level_does_not_have_trap_feeling() {
        Modifier modifier = provider.getTrapsGenerationModifier();

        assertEquals(Modifier.None, modifier);
    }

    @Test
    void provides_5x_modifier_when_level_have_trap_feeling() {
        Modifier expected = new Modifier(5f, 0f);
        level.feeling = Level.Feeling.TRAPS;

        Modifier modifier = provider.getTrapsGenerationModifier();

        assertEquals(expected, modifier);
    }
}