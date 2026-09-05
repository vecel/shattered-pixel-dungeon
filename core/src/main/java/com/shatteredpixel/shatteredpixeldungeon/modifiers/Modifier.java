package com.shatteredpixel.shatteredpixeldungeon.modifiers;

import java.util.Objects;

public class Modifier {
    protected float factor = 1f;
    protected float bonus = 0f;

    public static final Modifier None = new Modifier();

    public Modifier() {}

    public Modifier(float factor, float bonus) {
        this.factor = factor;
        this.bonus = bonus;
    }

    public float modify(float value) {
        return (value + bonus) * factor;
    }

    public float getFactor() {
        return factor;
    }

    public float getBonus() {
        return bonus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Modifier modifier = (Modifier) o;
        return Float.compare(factor, modifier.factor) == 0 && Float.compare(bonus, modifier.bonus) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(factor, bonus);
    }
}
