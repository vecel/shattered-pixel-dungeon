package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.modifiers.DamageModifier;

public interface PhysicalDamageTrap {

    int getDamage();

    void activateWithModifier(DamageModifier modifier);

    default int getModifiedDamage(DamageModifier modifier) {
        return modifier.apply(getDamage());
    }
}
