package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

import java.util.Objects;

public class LastKaboomTalent implements ArtifactUsedTalentHandler {
    @Override
    public void handleArtifactUsed(ArtifactUsedEvent event, int points) {
        if (!(event.artifact() instanceof Detonator)) return;

        Detonator detonator = (Detonator) event.artifact();
        Hero hero = event.hero();
        String action = event.action();

        if (!Objects.equals(action, Detonator.AC_ACTIVATE)) return;

        if (!detonator.didSpendCharge()) return;
        if (detonator.getCharge() > 0) return;

        if (points == 0) return;

        if (points >= 1) {
            hero.applyShielding(2);
        }
        if (points == 2) {
            hero.applyHealing(2);
        }
    }
}
