package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.fakes.HeroFake;

public class DetonatorContextFixtureWithHeroFake extends DetonatorContextFixture {
    public HeroFake hero;

    public DetonatorContextFixtureWithHeroFake() {
        super();
        this.hero = new HeroFake();
        this.context = new DetonatorContext(
                this.hero, this.dungeon, this.logger, this.registry, this.provider
        );
    }
}
