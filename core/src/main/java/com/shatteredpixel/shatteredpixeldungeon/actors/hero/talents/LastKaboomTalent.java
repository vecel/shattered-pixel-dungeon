package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

public class LastKaboomTalent implements ArtifactUsedTalentHandler {
    @Override
    public void handleArtifactUsed(Hero hero, Artifact artifact, int points) {
        if (!(artifact instanceof Detonator)) return;
        if (!((Detonator) artifact).didSpendCharge()) return;

        if (points == 0) return;

        if (points >= 1) {
            hero.applyShielding(2);
        }
        if (points == 2) {
            hero.applyHealing(2);
        }
    }
}
