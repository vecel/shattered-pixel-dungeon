package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;

public class TrapSettingTimeCalculator implements ActionTimeCalculator {
    @Override
    public float calculate(Hero hero) {
        int points = hero.pointsInTalent(Talent.TRAP_PROFICIENCY);
        if (points == 3) return 0.25f;
        if (points == 2) return 0.5f;
        return 1f;
    }
}
