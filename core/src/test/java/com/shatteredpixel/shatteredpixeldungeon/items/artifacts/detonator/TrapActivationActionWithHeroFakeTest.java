package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns.QuickActivationTalentCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrapActivationActionWithHeroFakeTest {

    private TrapActivationAction action;
    private DetonatorContextFixtureWithHeroFake fixture;

    @BeforeEach
    void setUp() {
        fixture = new DetonatorContextFixtureWithHeroFake();
        action = new TrapActivationAction(fixture.detonator, fixture.context);
    }

    @Test
    void takes_no_time_when_hero_has_quick_activation_talent() {
        fixture.hero.withTalent(Talent.QUICK_ACTIVATION, 1);

        action.execute(1);

        assertEquals(0f, fixture.hero.getTime());
        assertTrue(fixture.hero.hasBuff(QuickActivationTalentCooldown.class));
    }

    @Test
    void takes_time_when_hero_has_quick_activation_talent_with_cooldown() {
        fixture.hero.withTalent(Talent.QUICK_ACTIVATION, 1);
        fixture.hero.withBuff(new QuickActivationTalentCooldown());

        action.execute(1);

        assertEquals(1f, fixture.hero.getTime());
    }
}