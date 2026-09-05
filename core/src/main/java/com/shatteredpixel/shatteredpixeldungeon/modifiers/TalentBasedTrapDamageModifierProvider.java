package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import org.jetbrains.annotations.NotNull;

public class TalentBasedTrapDamageModifierProvider implements TrapDamageModifierProvider {
    @Override
    @NotNull
    public Modifier getDamageModifier(@NotNull Hero hero) {
        float bonus = 0f;
        if (hero.hasTalent(Talent.TRAP_EXPERT)) {
            bonus = 1 + hero.pointsInTalent(Talent.TRAP_EXPERT);
        }

        float factor = 1f;
        if (hero.hasTalent(Talent.HEAVY_AMMO)) {
            factor += 0.25f * hero.pointsInTalent(Talent.HEAVY_AMMO);
        }

        return new Modifier(factor, bonus);
    }
}
