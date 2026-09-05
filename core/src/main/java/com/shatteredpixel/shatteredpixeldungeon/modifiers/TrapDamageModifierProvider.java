package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

public interface TrapDamageModifierProvider {
    Modifier getDamageModifier(Hero hero);
}
