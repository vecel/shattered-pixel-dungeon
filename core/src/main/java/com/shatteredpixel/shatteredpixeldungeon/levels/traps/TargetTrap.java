package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;

public abstract class TargetTrap extends Trap {

    public Char findClosestCharacter() {
        Char target = Actor.findChar(pos);

        //find the closest char that can be aimed at
        //can't target beyond view distance, with a min of 6 (torch range)
        //add 0.5 for better consistency with vision radius shape
        float range = Math.max(6, Dungeon.level.viewDistance)+0.5f;
        if (target == null){
            float closestDist = Float.MAX_VALUE;
            for (Char ch : Actor.chars()){
                if (!ch.isAlive()) continue;
                float curDist = Dungeon.level.trueDistance(pos, ch.pos);
                //invis targets are considered to be at max range
                if (ch.invisible > 0) curDist = Math.max(curDist, range);
                Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
                if (bolt.collisionPos == ch.pos
                        && ( curDist < closestDist || (curDist == closestDist && target instanceof Hero))){
                    target = ch;
                    closestDist = curDist;
                }
            }
            if (closestDist > range){
                target = null;
            }
        }

        return target;
    }
}
