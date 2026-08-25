package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion.ExplosionStrategy;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion.SafeExplosionStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExplodingScrollsTalentTest {
    private ExplodingScrollsTalent talent;
    private Hero mockHero;
    private Bomb mockBomb;

    @BeforeEach
    void setUp() {
        talent = spy(new ExplodingScrollsTalent());

        mockBomb = mock(Bomb.class);
        mockHero = MockHero.create();

        when(mockHero.hasTalent(Talent.EXPLODING_SCROLLS)).thenReturn(true);
        doReturn(mockBomb).when(talent).createBomb();
    }

    @Test
    void triggers_3x3_square_explosion_when_talent_has_one_point() {
        when(mockHero.pointsInTalent(Talent.EXPLODING_SCROLLS)).thenReturn(1);

        talent.handleScrollRead(mockHero, -1, null);

        verify(mockBomb, times(1)).explode(-1, new SafeExplosionStrategy(1));
    }

    @Test
    void triggers_5x5_circle_explosion_when_talent_has_two_points() {
        when(mockHero.pointsInTalent(Talent.EXPLODING_SCROLLS)).thenReturn(2);

        talent.handleScrollRead(mockHero, -1, null);

        fail("Not implemented circle safe explosion strategy");

        verify(mockBomb, times(1)).explode(-1, new SafeExplosionStrategy(1));
    }

    @Test
    void does_nothing_when_hero_does_not_have_talent() {
        when(mockHero.hasTalent(Talent.EXPLODING_SCROLLS)).thenReturn(false);

        talent.handleScrollRead(mockHero, -1, null);

        verify(mockBomb, never()).explode(any(Integer.class), any(ExplosionStrategy.class));
    }
}