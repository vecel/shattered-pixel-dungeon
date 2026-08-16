package com.shatteredpixel.shatteredpixeldungeon.fakes.hero;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

public class MockHero {

    public final Hero hero;
    public final CharSprite sprite;
    public final Belongings belongings;

    public MockHero() {
        this.hero = mock(Hero.class);
        this.sprite = mock(CharSprite.class);
        this.belongings = mock(Belongings.class);


        hero.sprite = sprite;
        hero.belongings = belongings;
    }

    public static Hero create() {
        return new MockHero().hero;
    }
}
