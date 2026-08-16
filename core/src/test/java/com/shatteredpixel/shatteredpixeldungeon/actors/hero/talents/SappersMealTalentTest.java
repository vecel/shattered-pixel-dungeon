package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.fakes.hero.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SappersMealTalentTest {

    private SappersMealTalent talent;
    private Hero mockHero;
    private Detonator mockDetonator;

    @BeforeEach
    void setUp() {
        talent = new SappersMealTalent();

        mockHero = MockHero.create();
        mockDetonator = mock(Detonator.class);

        when(mockHero.belongings.getItem(Detonator.class)).thenReturn(mockDetonator);
        when(mockHero.hasTalent(Talent.SAPPERS_MEAL)).thenReturn(true);
    }

    @Test
    void charges_detonator_with_half_charge_when_talent_has_one_point() {
        when(mockHero.pointsInTalent(Talent.SAPPERS_MEAL)).thenReturn(1);

        talent.handleFoodEaten(mockHero, -1, null);

        verify(mockDetonator, times(1)).charge(mockHero, (float) 0.5);
    }

    @Test
    void charges_detonator_with_one_charge_when_talent_has_two_points() {
        when(mockHero.pointsInTalent(Talent.SAPPERS_MEAL)).thenReturn(2);

        talent.handleFoodEaten(mockHero, -1, null);

        verify(mockDetonator, times(1)).charge(mockHero, 1);
    }

    @Test
    void does_nothing_when_hero_does_not_have_detonator() {
        when(mockHero.belongings.getItem(Detonator.class)).thenReturn(null);

        talent.handleFoodEaten(mockHero, -1, null);

        verify(mockHero, never()).pointsInTalent(Talent.SAPPERS_MEAL);
        verify(mockDetonator, never()).charge(any(Hero.class), any(Float.class));
    }

    @Test
    void does_nothing_when_hero_does_not_have_sappers_meal_talent() {
        when(mockHero.hasTalent(Talent.SAPPERS_MEAL)).thenReturn(false);

        talent.handleFoodEaten(mockHero, -1, null);

        verify(mockDetonator, never()).charge(any(Hero.class), any(Float.class));
    }
}