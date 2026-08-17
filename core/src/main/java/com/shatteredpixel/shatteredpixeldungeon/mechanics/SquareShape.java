package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class SquareShape extends Shape {

    /**
     * Returns cells in a square shape of odd size.
     *
     * @param radius number of tiles from square center to edge. If you wish to create 5x5 square
     *               area, set radius to 2.
     */
    public SquareShape(int radius) {
        super(radius);
    }

    @Override
    public List<Integer> getCells(int center) {
        List<Integer> cells = new ArrayList<>();
        Point c = Dungeon.level.cellToPoint(center);
        int left, right;
        int curr;
        for (int y = Math.max(0, c.y - radius); y <= Math.min(Dungeon.level.height()-1, c.y + radius); y++) {
            left = c.x - radius;
            right = Math.min(Dungeon.level.width() - 1, c.x + c.x - left);
            left = Math.max(0, left);
            for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++) cells.add(curr);
        }

        return cells;
    }
}
