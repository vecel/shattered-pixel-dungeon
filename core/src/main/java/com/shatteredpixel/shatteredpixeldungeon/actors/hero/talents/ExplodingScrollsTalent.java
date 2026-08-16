package com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion.ExplosionStrategy;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion.SafeExplosionStrategy;

public class ExplodingScrollsTalent implements ScrollTalentHandler {
    @Override
    public void handleScrollRead(Hero hero, int pos, Class<? extends Item> itemClass) {
        if (!hero.hasTalent(Talent.EXPLODING_SCROLLS)) return;

        // TODO: Implement 5x5 round if hero has 2 points in talent
        int points = hero.pointsInTalent(Talent.EXPLODING_SCROLLS);
        ExplosionStrategy strategy = new SafeExplosionStrategy(1);

        Bomb bomb = createBomb();
        bomb.explode(pos, strategy);
    }

    protected Bomb createBomb() {
        return new Bomb();
    }
}
