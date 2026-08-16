package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;

public interface ScrollTalentHandler {

    void handleScrollRead(Hero hero, int pos, Class<? extends Item> itemClass);
}
