package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import java.util.List;
import java.util.Objects;

public abstract class Shape {

    protected final int radius;

    protected Shape(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public abstract List<Integer> getCells(int center);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return radius == shape.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(radius);
    }
}
