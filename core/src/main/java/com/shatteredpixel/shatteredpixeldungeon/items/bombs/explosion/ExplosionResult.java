package com.shatteredpixel.shatteredpixeldungeon.items.bombs.explosion;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;

import java.util.ArrayList;
import java.util.List;

public class ExplosionResult {

    public final List<Integer> affectedCells;
    public final List<Char> affectedChars;

    public ExplosionResult() {
        this.affectedCells = new ArrayList<>();
        this.affectedChars = new ArrayList<>();
    }

    public ExplosionResult(List<Integer> affectedCells, List<Char> affectedChars) {
        this.affectedCells = affectedCells;
        this.affectedChars = affectedChars;
    }
}
