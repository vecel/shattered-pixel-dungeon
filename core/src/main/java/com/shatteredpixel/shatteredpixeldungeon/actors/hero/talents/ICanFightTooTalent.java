package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;

public class ICanFightTooTalent implements ArtifactUsedTalentHandler {
    @Override
    public void handleArtifactUsed(Hero hero, Artifact artifact, int points) {
        if (!hero.hasTalent(Talent.I_CAN_FIGHT_TOO)) return;
        hero.applyBuff(Talent.ICanFightTooTracker.class);
    }
}
