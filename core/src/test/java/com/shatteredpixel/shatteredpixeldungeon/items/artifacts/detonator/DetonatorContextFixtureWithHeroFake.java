package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.fakes.FakeHero;

public class DetonatorContextFixtureWithHeroFake extends DetonatorContextFixture {
    public FakeHero hero;

    public DetonatorContextFixtureWithHeroFake() {
        super();
        this.hero = new FakeHero();
        this.context = new DetonatorContext(
                this.hero, this.dungeon, this.logger, this.registry, this.provider
        );
    }
}
