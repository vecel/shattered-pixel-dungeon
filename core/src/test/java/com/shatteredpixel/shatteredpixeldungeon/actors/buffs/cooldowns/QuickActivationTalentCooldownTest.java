package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.fixtures.EngineFixture;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class QuickActivationTalentCooldownTest {

    private EngineFixture engine;
    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = spy(Hero.class);

        doAnswer(invocation -> {
            hero.spend(1f);
            return true;
        }).when(hero).act();

        engine = new EngineFixture();
        engine.setUp();
        engine.add(hero);
    }

    @AfterEach
    void tearDown() {
        engine.tearDown();
    }

    @Test
    void cooldown_lasts_given_time() {
        hero.applyCooldownBuff(new QuickActivationTalentCooldown(), 1f);

        QuickActivationTalentCooldown cooldown = (QuickActivationTalentCooldown) engine.getActorById(2);

        assertEquals(2, engine.getActorCount());
        assertTrue(engine.hasActor(hero));
        assertTrue(engine.hasActor(cooldown));

        assertEquals(0f, hero.getTime());
        assertEquals(1f, cooldown.getTime());

        engine.processNext();
        engine.processNext();

        assertTrue(hero.hasBuff(QuickActivationTalentCooldown.class));
        assertTrue(engine.hasActor(cooldown));
        assertEquals(2f, hero.getTime());
        assertEquals(1f, cooldown.getTime());

        engine.processNext();

        assertFalse(engine.hasActor(cooldown));
    }
}