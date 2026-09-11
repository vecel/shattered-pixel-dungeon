package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.energies;

import static org.junit.jupiter.api.Assertions.*;

import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManager;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.fakes.ActiveActionManagerFake;
import com.shatteredpixel.shatteredpixeldungeon.fakes.HeroFake;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EngineerEnergyBuffTest {

    private EngineerEnergyBuff buff;
    private HeroFake hero;
    private ActiveActionManagerFake actionManager;

    @BeforeEach
    void setUp() {
        actionManager = new ActiveActionManagerFake();
        hero = new HeroFake();

        buff = new EngineerEnergyBuff(actionManager);
    }
    
    @Test
    void subscribes_to_artifact_used_events_when_attached_to_hero() {
        buff.attachTo(hero);

        assertTrue(hero.hasListener(buff));
    }
    
    @Test
    void unsubscribes_to_hero_when_detached() {
        buff.attachTo(hero);
        buff.detach();

        assertFalse(hero.hasListener(buff));
    }

    @Test
    void adds_energy_point_when_activated_trap_with_detonator() {
        ArtifactUsedEvent event = new ArtifactUsedEvent(hero, new Detonator(), Detonator.AC_ACTIVATE);
        buff.setEnergy(0f);

        buff.onEvent(event);

        assertEquals(1f, buff.getEnergy());
    }

    @Test
    void sets_itself_an_active_action_when_has_positive_amount_of_energy() {
        buff.setEnergy(1f);
        buff.act();

        assertEquals(buff, actionManager.getAction());
    }

    @Test
    void clears_itself_from_active_action_when_used_and_energy_is_zero() {
        actionManager.set(buff);
        buff.setEnergy(0f);

        buff.doAction();

        assertNull(actionManager.getAction());
    }
}