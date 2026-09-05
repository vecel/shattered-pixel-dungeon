package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

public class TrapDamageModifierProviderAdapter implements TrapDamageModifierProvider {

    private final TrapDamageModifierProvider provider = new TalentBasedTrapDamageModifierProvider();

    @Override
    public Modifier getDamageModifier(Hero hero) {
        return provider.getDamageModifier(hero);
    }
}
