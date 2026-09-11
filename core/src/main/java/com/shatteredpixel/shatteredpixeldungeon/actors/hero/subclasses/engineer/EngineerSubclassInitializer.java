package com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.engineer;

import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManager;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.energies.EngineerEnergyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.SubclassInitializer;

public class EngineerSubclassInitializer implements SubclassInitializer {

    private final ActiveActionManager action;

    public EngineerSubclassInitializer(ActiveActionManager action) {
        this.action = action;
    }

    @Override
    public void initialize(Hero hero) {
        EngineerEnergyBuff energy = new EngineerEnergyBuff(action);
        hero.applyBuff(energy);
    }
}
