package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.SquareShape;

public class ShapeUtils {

    public static Shape max(Shape a, Shape b) {
        if (a.getRadius() > b.getRadius()) return a;
        if (a.getRadius() < b.getRadius()) return b;

        if (a instanceof SquareShape) return a;
        return b;
    }
}
