package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ChargeUsageCalculator;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrapSettingChargeCalculatorTest {

    private Hero hero;
    private ChargeUsageCalculator calculator;

    @BeforeEach
    void setUp() {
        hero = MockHero.create();

        calculator = new TrapSettingChargeCalculator();
    }

    @Test
    void returns_one_when_hero_has_trap_proficiency_talent() {
        when(hero.hasTalent(Talent.TRAP_PROFICIENCY)).thenReturn(true);

        int result = calculator.calculate(hero);

        assertEquals(1, result);
    }

    @Test
    void returns_two_when_hero_does_not_have_trap_proficiency_talent() {
        when(hero.hasTalent(Talent.TRAP_PROFICIENCY)).thenReturn(false);

        int result = calculator.calculate(hero);

        assertEquals(2, result);
    }
}