package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;

public interface ArtifactUsedTalentHandler {

    void handleArtifactUsed(Hero hero, Artifact artifact, int points);
}
