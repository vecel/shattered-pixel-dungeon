package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;

public interface FoodTalentHandler {
    void handleFoodEaten(Hero hero, float foodVal, Item foodSource);
}
