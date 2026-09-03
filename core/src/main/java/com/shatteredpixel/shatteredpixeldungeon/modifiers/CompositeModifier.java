package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import java.util.Arrays;
import java.util.List;

public class CompositeModifier extends Modifier {

    private final List<Modifier> modifiers;

    public CompositeModifier(Modifier... modifiers) {
        this.modifiers = Arrays.asList(modifiers);
    }

    @Override
    public float modify(float value) {
        float bonus = 0;
        float factor = 1;
        for (Modifier modifier : modifiers) {
            bonus += modifier.bonus;
            factor *= modifier.factor;
        }
        return (value + bonus) * factor;
    }
}
