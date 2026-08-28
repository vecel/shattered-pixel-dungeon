package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns.QuickActivationTalentCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

public class QuickActivationTalent implements ArtifactUsedTalentHandler {
    @Override
    public void handleArtifactUsed(Hero hero, Artifact artifact, int points) {
        if (!(artifact instanceof Detonator)) return;
        if (hero.hasBuff(QuickActivationTalentCooldown.class)) return;

        int cooldown = points == 1 ? 50 : 30;

//        logger.positive("That was a quick!");

        hero.applyCooldownBuff(new QuickActivationTalentCooldown(), cooldown - 1);
    }
}
