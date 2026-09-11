package com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.engineer;

import com.karandys.shatteredpixeldungeon.hero.subclasses.engineer.sprites.CrossbowConstructSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;

public class CrossbowConstruct extends Construct {

    private int range;

    public CrossbowConstruct() {
        spriteClass = CrossbowConstructSprite.class;
    }

//    @Override
//    protected boolean act() {
//        // Do not forget to spend time
//        Char enemy = chooseEnemy();
//        if (enemy == null) return true;
//        // find closest enemy in range
//        // shoot
//        return true;
//    }
}
