package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns.QuickActivationTalentCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;

public class TrapActivationTimeCalculator implements ActionTimeCalculator {
    @Override
    public float calculate(Hero hero) {
        if (hero.hasTalent(Talent.QUICK_ACTIVATION) && !hero.hasBuff(QuickActivationTalentCooldown.class)) return 0f;
        return 1f;
    }
}
