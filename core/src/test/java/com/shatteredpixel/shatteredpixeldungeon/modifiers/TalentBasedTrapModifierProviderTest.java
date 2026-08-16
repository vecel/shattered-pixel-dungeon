package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TalentBasedTrapModifierProviderTest {

    private TrapModifierProvider provider;
    private Hero mockHero;

    @BeforeEach
    void setUp() {
        mockHero = mock(Hero.class);

        provider = new TalentBasedTrapModifierProvider();
    }

    @Test
    void applies_two_bonus_damage_when_hero_has_trap_expert_talent_at_level_one() {
        when(mockHero.hasTalent(Talent.TRAP_EXPERT)).thenReturn(true);
        when(mockHero.pointsInTalent(Talent.TRAP_EXPERT)).thenReturn(1);

        DamageModifier modifier = provider.getModifierFor(mockHero);

        assertEquals(2, modifier.getModifier());
    }

    @Test
    void applies_three_bonus_damage_when_hero_has_trap_expert_talent_at_level_two() {
        when(mockHero.hasTalent(Talent.TRAP_EXPERT)).thenReturn(true);
        when(mockHero.pointsInTalent(Talent.TRAP_EXPERT)).thenReturn(2);

        DamageModifier modifier = provider.getModifierFor(mockHero);

        assertEquals(3, modifier.getModifier());
    }

    @Test
    void applies_nothing_when_hero_does_not_have_trap_expert_talent() {
        when(mockHero.hasTalent(Talent.TRAP_EXPERT)).thenReturn(false);

        DamageModifier modifier = provider.getModifierFor(mockHero);

        assertEquals(DamageModifier.NONE, modifier);
    }
}