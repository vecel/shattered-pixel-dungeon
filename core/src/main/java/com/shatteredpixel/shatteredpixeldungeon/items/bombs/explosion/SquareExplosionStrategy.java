package com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

import org.jetbrains.annotations.NotNull;

public class SquareExplosionStrategy implements ExplosionStrategy {

    private final int range;

    public SquareExplosionStrategy(int range) {
        this.range = range;
    }

    @Override
    @NotNull
    public ExplosionResult calculateExplosionArea(int cell) {
        ExplosionResult result = new ExplosionResult();

        boolean[] explodable = new boolean[Dungeon.level.length()];
        BArray.not( Dungeon.level.solid, explodable);
        BArray.or( Dungeon.level.flamable, explodable, explodable);
        PathFinder.buildDistanceMap(cell, explodable, range);

        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] != Integer.MAX_VALUE) {
                result.affectedCells.add(i);
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    result.affectedChars.add(ch);
                }
            }
        }

        return result;
    }
}
