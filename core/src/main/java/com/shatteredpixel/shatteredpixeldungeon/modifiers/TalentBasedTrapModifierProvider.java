package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import org.jetbrains.annotations.NotNull;

public class TalentBasedTrapModifierProvider implements TrapModifierProvider {
    @Override
    @NotNull
    public DamageModifier getModifierFor(@NotNull Hero hero) {
        if (!hero.hasTalent(Talent.TRAP_EXPERT)) return DamageModifier.NONE;
        int points = hero.pointsInTalent(Talent.TRAP_EXPERT);

        return new DamageModifier(points + 1);
    }
}
