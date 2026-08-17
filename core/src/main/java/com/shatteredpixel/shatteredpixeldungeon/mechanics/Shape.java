package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import java.util.List;

public abstract class Shape {

    protected final int radius;

    protected Shape(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public abstract List<Integer> getCells(int center);
}
