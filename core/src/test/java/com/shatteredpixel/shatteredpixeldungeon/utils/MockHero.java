package com.shatteredpixel.shatteredpixeldungeon.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

public class MockHero {

    public final Hero hero;
    public final CharSprite sprite;
    public final Belongings belongings;
    public final Hunger hunger;

    public MockHero() {
        this.hero = mock(Hero.class);
        this.sprite = mock(CharSprite.class);
        this.belongings = mock(Belongings.class);
        this.hunger = mock(Hunger.class);

        hero.sprite = sprite;
        hero.belongings = belongings;

        doNothing().when(sprite).operate(any(Integer.class));
        doNothing().when(sprite).showStatus(any(Integer.class), any(String.class));

        when(hero.getBuff(Hunger.class)).thenReturn(hunger);
        when(hero.affectBuff(Hunger.class)).thenReturn(hunger);
    }

    public static Hero create() {
        return new MockHero().hero;
    }
}
