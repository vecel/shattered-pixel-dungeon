package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ChargeUsageCalculator;

public class TrapSettingChargeCalculator implements ChargeUsageCalculator {
    @Override
    public int calculate(Hero hero) {
        if (hero.hasTalent(Talent.TRAP_PROFICIENCY)) return 1;
        return 2;
    }
}
