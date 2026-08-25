package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class QuickActivationTalentCooldown extends FlavourBuff {
    @Override
    public int icon() {
        return BuffIndicator.DETONATOR;
    }
    public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
}
