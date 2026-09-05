package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TalentBasedTrapDamageModifierProviderTest {

    private TrapDamageModifierProvider provider;
    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = MockHero.create();

        provider = new TalentBasedTrapDamageModifierProvider();
    }

    @Test
    void applies_two_bonus_damage_when_hero_has_trap_expert_talent_at_level_one() {
        when(hero.hasTalent(Talent.TRAP_EXPERT)).thenReturn(true);
        when(hero.pointsInTalent(Talent.TRAP_EXPERT)).thenReturn(1);

        Modifier modifier = provider.getDamageModifier(hero);

        assertEquals(2, modifier.getBonus());
    }

    @Test
    void applies_three_bonus_damage_when_hero_has_trap_expert_talent_at_level_two() {
        when(hero.hasTalent(Talent.TRAP_EXPERT)).thenReturn(true);
        when(hero.pointsInTalent(Talent.TRAP_EXPERT)).thenReturn(2);

        Modifier modifier = provider.getDamageModifier(hero);

        assertEquals(3, modifier.getBonus());
    }

    @Test
    void applies_nothing_when_hero_does_not_have_trap_expert_talent() {
        Modifier modifier = provider.getDamageModifier(hero);

        assertEquals(Modifier.None, modifier);
    }

    @Test
    void applies_damage_factor_when_hero_has_heavy_ammo_talent_at_level_one() {
        when(hero.hasTalent(Talent.HEAVY_AMMO)).thenReturn(true);
        when(hero.pointsInTalent(Talent.HEAVY_AMMO)).thenReturn(1);

        Modifier modifier = provider.getDamageModifier(hero);

        assertEquals(1.25f, modifier.getFactor());
    }

    @Test
    void applies_damage_factor_when_hero_has_heavy_ammo_talent_at_level_two() {
        when(hero.hasTalent(Talent.HEAVY_AMMO)).thenReturn(true);
        when(hero.pointsInTalent(Talent.HEAVY_AMMO)).thenReturn(2);

        Modifier modifier = provider.getDamageModifier(hero);

        assertEquals(1.5f, modifier.getFactor());
    }

    @Test
    void applies_damage_factor_when_hero_has_heavy_ammo_talent_at_level_three() {
        when(hero.hasTalent(Talent.HEAVY_AMMO)).thenReturn(true);
        when(hero.pointsInTalent(Talent.HEAVY_AMMO)).thenReturn(3);

        Modifier modifier = provider.getDamageModifier(hero);

        assertEquals(1.75f, modifier.getFactor());
    }
}