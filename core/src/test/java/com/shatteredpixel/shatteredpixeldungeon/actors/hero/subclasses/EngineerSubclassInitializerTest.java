package com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses;

import static org.junit.jupiter.api.Assertions.*;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.energies.EngineerEnergyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.engineer.EngineerSubclassInitializer;
import com.shatteredpixel.shatteredpixeldungeon.fakes.ActiveActionManagerFake;
import com.shatteredpixel.shatteredpixeldungeon.fakes.HeroFake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EngineerSubclassInitializerTest {

    private EngineerSubclassInitializer initializer;
    private ActiveActionManagerFake actionManager;
    private HeroFake hero;

    @BeforeEach
    void setUp() {
        actionManager = new ActiveActionManagerFake();
        hero = new HeroFake();

        initializer = new EngineerSubclassInitializer(actionManager);
    }

    @Test
    void applies_engineer_energy_buff() {
        initializer.initialize(hero);

        assertTrue(hero.hasBuff(EngineerEnergyBuff.class));
    }
}