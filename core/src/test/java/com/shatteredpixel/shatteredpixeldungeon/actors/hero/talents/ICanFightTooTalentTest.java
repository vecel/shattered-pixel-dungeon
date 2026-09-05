package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ICanFightTooTalentTest {

    private ICanFightTooTalent talent;
    private ArtifactUsedEvent event;
    private Hero hero;
    private Detonator detonator;

    @BeforeEach
    void setUp() {
        talent = new ICanFightTooTalent();

        hero = MockHero.create();
        detonator = new Detonator();
        event = new ArtifactUsedEvent(hero, detonator, Detonator.AC_ACTIVATE);
    }

    @Test
    void applies_talent_tracker_buff() {
        talent.handleArtifactUsed(event, 1);

        verifyOnce(hero).applyBuff(ICanFightTooTalent.Tracker.class);
    }

    @Test
    void triggers_when_detonator_activate_action_is_used_only() {
        event = new ArtifactUsedEvent(hero, detonator, "UNKNOWN");

        talent.handleArtifactUsed(event, 1);

        verifyNever(hero).applyBuff(ICanFightTooTalent.Tracker.class);
    }
}