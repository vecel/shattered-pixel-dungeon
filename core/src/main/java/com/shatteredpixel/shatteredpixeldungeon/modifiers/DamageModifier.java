package com.shatteredpixel.shatteredpixeldungeon.modifiers;

public class DamageModifier {

	private final int modifier;
	private final float multiplier;

	public DamageModifier() {
		this.modifier = 0;
		this.multiplier = 1;
	}

	public DamageModifier(int modifier) {
		this.modifier = modifier;
		this.multiplier = 1;
	}

    public DamageModifier(int modifier, float multiplier) {
		this.modifier = modifier;
		this.multiplier = multiplier;
    }

	public int apply(int damage) {
		return Math.max(0, Math.round((damage + modifier) * multiplier));
	}
}
