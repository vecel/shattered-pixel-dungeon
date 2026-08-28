package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns.QuickActivationTalentCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrapActivationTimeCalculatorTest {

    private Hero hero;
    private ActionTimeCalculator calculator;

    @BeforeEach
    void setUp() {
        hero = MockHero.create();

        calculator = new TrapActivationTimeCalculator();
    }

    @Test
    void returns_zero_when_hero_has_quick_activation_talent() {
        when(hero.hasTalent(Talent.QUICK_ACTIVATION)).thenReturn(true);

        float time = calculator.calculate(hero);

        assertEquals(0f, time);
    }

    @Test
    void returns_one_when_hero_has_talent_cooldown() {
        when(hero.hasBuff(QuickActivationTalentCooldown.class)).thenReturn(true);

        float time = calculator.calculate(hero);

        assertEquals(1f, time);
    }

    @Test
    void returns_one_when_hero_does_not_have_quick_activation_talent() {
        when(hero.hasTalent(Talent.QUICK_ACTIVATION)).thenReturn(false);

        float time = calculator.calculate(hero);

        assertEquals(1f, time);
    }
}