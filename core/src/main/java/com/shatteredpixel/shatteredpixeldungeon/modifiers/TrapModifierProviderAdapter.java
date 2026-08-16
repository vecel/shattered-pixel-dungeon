package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

public class TrapModifierProviderAdapter implements TrapModifierProvider {

    private final TrapModifierProvider provider = new TalentBasedTrapModifierProvider();

    @Override
    public DamageModifier getModifierFor(Hero hero) {
        return provider.getModifierFor(hero);
    }
}
