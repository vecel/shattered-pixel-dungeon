package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

public class SappersMealTalent implements FoodTalentHandler {

    @Override
    public void handleFoodEaten(Hero hero, float foodVal, Item foodSource) {
        if (!hero.hasTalent(Talent.SAPPERS_MEAL)) return;

        Detonator detonator = hero.belongings.getItem(Detonator.class);
        if (detonator == null) return;

        int points = hero.pointsInTalent(Talent.SAPPERS_MEAL);
        detonator.charge(hero, (float) (points / 2.0));
    }
}
