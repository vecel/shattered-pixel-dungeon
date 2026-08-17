package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class CircularShape extends Shape {

    public CircularShape(int radius) {
        super(radius);
    }

    @Override
    public List<Integer> getCells(int center) {
        List<Integer> cells = new ArrayList<>();

        Point c = Dungeon.level.cellToPoint(center);
        int[] rounding = ShadowCaster.rounding[radius];

        int left, right;
        int curr;
        for (int y = Math.max(0, c.y - radius); y <= Math.min(Dungeon.level.height()-1, c.y + radius); y++) {
            if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
                left = c.x - rounding[Math.abs(c.y - y)];
            } else {
                left = radius;
                while (rounding[left] < rounding[Math.abs(c.y - y)]) {
                    left--;
                }
                left = c.x - left;
            }
            right = Math.min(Dungeon.level.width() - 1, c.x + c.x - left);
            left = Math.max(0, left);
            for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++) cells.add(curr);
        }
        return cells;
    }
}
