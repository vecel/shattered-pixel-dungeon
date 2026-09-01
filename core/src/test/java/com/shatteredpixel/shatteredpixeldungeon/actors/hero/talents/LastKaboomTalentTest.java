package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LastKaboomTalentTest {

    private LastKaboomTalent talent;
    private Hero hero;
    private Detonator detonator;
    private int points;
    private int shielding;
    private int healing;

    @BeforeEach
    void setUp() {
        hero = MockHero.create();
        detonator = mock(Detonator.class);

        points = 1;
        shielding = 2;
        healing = 2;

        talent = new LastKaboomTalent();

        when(detonator.didSpendCharge()).thenReturn(true);
    }

    @Test
    void applies_shielding_when_talent_has_one_point() {
        when(detonator.getCharge()).thenReturn(0);

        talent.handleArtifactUsed(hero, detonator, points);

        verifyOnce(hero).applyShielding(shielding);
    }

    @Test
    void applies_shielding_and_healing_when_talent_has_two_points() {
        points = 2;
        when(detonator.getCharge()).thenReturn(0);

        talent.handleArtifactUsed(hero, detonator, points);

        verifyOnce(hero).applyShielding(shielding);
        verifyOnce(hero).applyHealing(healing);
    }

    @Test
    void dose_not_trigger_when_last_charge_was_not_spend() {
        when(detonator.getCharge()).thenReturn(1);

        talent.handleArtifactUsed(hero, detonator, points);

        verifyNever(hero).applyShielding(anyInt());
    }

    @Test
    void does_not_trigger_when_spent_no_charge() {
        when(detonator.didSpendCharge()).thenReturn(false);

        talent.handleArtifactUsed(hero, detonator, points);

        verifyNever(hero).applyShielding(anyInt());
    }

    @Test
    void does_not_trigger_when_trap_is_stored_or_set() {
        ArtifactUsedEvent event = new ArtifactUsedEvent(hero, detonator, Detonator.AC_STORE_TRAP);

        talent.handleArtifactUsed(event, points);

        verifyNever(hero).applyShielding(anyInt());
        verifyNever(hero).applyHealing(anyInt());
    }
}