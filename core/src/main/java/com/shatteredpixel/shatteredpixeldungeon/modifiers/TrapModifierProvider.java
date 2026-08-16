package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

public interface TrapModifierProvider {
    DamageModifier getModifierFor(Hero hero);
}
