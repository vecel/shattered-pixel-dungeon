package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class ICanFightTooTalent implements ArtifactUsedTalentHandler {

    @Override
    public void handleArtifactUsed(ArtifactUsedEvent event, int points) {
        if (!event.action().equals(Detonator.AC_ACTIVATE)) return;

        Hero hero = event.hero();
        hero.applyBuff(Tracker.class);
    }

    public static class Tracker extends Buff {
        { type = Buff.buffType.POSITIVE; }
        public int icon() { return BuffIndicator.INVERT_MARK; }
    }
}

