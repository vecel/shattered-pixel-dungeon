package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeroTrapsGenerationModifierProviderTest {

    private HeroTrapsGenerationModifierProvider provider;
    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = MockHero.create();
        provider = new HeroTrapsGenerationModifierProvider(hero);
    }
    
    @Test
    void provides_default_modifier_when_hero_is_null() {
        provider = new HeroTrapsGenerationModifierProvider(null);

        Modifier modifier = provider.getTrapsGenerationModifier();

        assertEquals(Modifier.None, modifier);
    }
    
    @Test
    void provides_default_modifier_when_hero_does_not_have_explosion_will_talent() {
        Modifier modifier = provider.getTrapsGenerationModifier();

        assertEquals(Modifier.None, modifier);
    }
    
    @Test
    void provides_correct_modifier_when_hero_has_explosion_will_talent() {
        when(hero.hasTalent(Talent.EXPLOSION_WILL)).thenReturn(true);
        when(hero.pointsInTalent(Talent.EXPLOSION_WILL)).thenReturn(3);

        Modifier expected = new Modifier(1.75f, 0f);

        Modifier modifier = provider.getTrapsGenerationModifier();

        assertEquals(expected, modifier);
    }
}