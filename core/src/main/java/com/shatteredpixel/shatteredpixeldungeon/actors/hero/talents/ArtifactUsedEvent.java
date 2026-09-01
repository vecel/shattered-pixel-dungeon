package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;

public class ArtifactUsedEvent {
    private Hero hero;
    private Artifact artifact;
    private String action;

    public ArtifactUsedEvent(Hero hero, Artifact artifact, String action) {
        this.hero = hero;
        this.artifact = artifact;
        this.action = action;
    }

    public Hero hero() {
        return hero;
    }

    public Artifact artifact() {
        return artifact;
    }

    public String action() {
        return action;
    }
}

