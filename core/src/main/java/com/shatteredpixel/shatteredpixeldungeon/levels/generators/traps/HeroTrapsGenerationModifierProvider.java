package com.shatteredpixel.shatteredpixeldungeon.levels.generators.traps;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;

public class HeroTrapsGenerationModifierProvider implements TrapsGenerationModifierProvider {

    private final Hero hero;

    public HeroTrapsGenerationModifierProvider(Hero hero) {
        this.hero = hero;
    }

    @Override
    public Modifier getTrapsGenerationModifier() {
        if (hero == null) return Modifier.None;
        if (!hero.hasTalent(Talent.EXPLOSION_WILL)) return Modifier.None;

        int points = hero.pointsInTalent(Talent.EXPLOSION_WILL);
        float factor = 1f + points * 0.25f;
        return new Modifier(factor, 0f);
    }
}
