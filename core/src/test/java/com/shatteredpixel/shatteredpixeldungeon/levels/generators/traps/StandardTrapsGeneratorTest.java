package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.watabou.utils.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class StandardTrapsGeneratorTest {

    private StandardTrapsGenerator generator;
    private TrapsGenerationModifierProvider provider;
    private Level level;

    @BeforeEach
    void setUp() {
        generator = new StandardTrapsGenerator();

        provider = mock(TrapsGenerationModifierProvider.class);
        level = mock(Level.class);

        when(level.getDepth()).thenReturn(5);
    }

    @Test
    void applies_provided_modifier_to_traps_count() {
        when(provider.getTrapsGenerationModifier()).thenReturn(new Modifier(2f, 0f));
        try (MockedStatic<Random> mockedRandom = mockStatic(Random.class)) {
            mockedRandom.when(() -> Random.NormalIntRange(anyInt(), anyInt())).thenReturn(4);

            int result = generator.getCount(level, provider);

            assertEquals(8, result);
        }
    }
}