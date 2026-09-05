package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;

public interface PhysicalDamageTrap {

    int getDamage();

    void activateWithModifier(Modifier modifier);

    default int getModifiedDamage(Modifier modifier) {
        return (int) modifier.modify(getDamage());
    }
}
