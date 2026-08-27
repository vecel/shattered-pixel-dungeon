package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;

import java.util.List;

public abstract class DetonatorAction {

    protected final Detonator detonator;
    protected final DetonatorContext context;

    public DetonatorAction(Detonator detonator, DetonatorContext context) {
        this.detonator = detonator;
        this.context = context;
    }

    public abstract void execute(int cell);
    public abstract List<Integer> availableCells();
    public abstract String prompt();
}
