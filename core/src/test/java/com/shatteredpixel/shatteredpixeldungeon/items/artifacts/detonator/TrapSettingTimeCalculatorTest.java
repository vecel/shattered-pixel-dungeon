package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrapSettingTimeCalculatorTest {

    private Hero hero;
    private ActionTimeCalculator calculator;

    @BeforeEach
    void setUp() {
        hero = MockHero.create();

        calculator = new TrapSettingTimeCalculator();
    }

    @Test
    void returns_zero_point_two_five_when_hero_has_three_points_in_trap_proficiency() {
        when(hero.pointsInTalent(Talent.TRAP_PROFICIENCY)).thenReturn(3);

        float time = calculator.calculate(hero);

        assertEquals(0.25f, time);
    }

    @Test
    void returns_zero_point_five_when_hero_has_two_points_in_trap_proficiency() {
        when(hero.pointsInTalent(Talent.TRAP_PROFICIENCY)).thenReturn(2);

        float time = calculator.calculate(hero);

        assertEquals(0.5f, time);
    }

    @Test
    void returns_one_when_hero_has_one_point_in_trap_proficiency() {
        when(hero.pointsInTalent(Talent.TRAP_PROFICIENCY)).thenReturn(1);

        float time = calculator.calculate(hero);

        assertEquals(1f, time);
    }

    @Test
    void returns_one_when_hero_has_no_points_in_trap_proficiency() {
        when(hero.pointsInTalent(Talent.TRAP_PROFICIENCY)).thenReturn(0);

        float time = calculator.calculate(hero);

        assertEquals(1f, time);
    }
}