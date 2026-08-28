package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.SquareShape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.List;

public class TrapStorageAction extends DetonatorAction {

    private final Shape range;

    public TrapStorageAction(Detonator detonator, DetonatorContext context) {
        super(detonator, context);
        this.range = new SquareShape(1);
    }

    TrapStorageAction(Detonator detonator, DetonatorContext context, Shape range) {
        super(detonator, context);
        this.range = range;
    }

    @Override
    public void execute(int cell) {
        Hero hero = context.getHero();
        DungeonInterface dungeon = context.getDungeon();

        if (!hero.withinFieldOfView(cell)) return;
        if (!dungeon.hasVisibleTrapAt(cell)) return;

        Trap trap = dungeon.getTrap(cell);
        if (!trap.isActive()) return;

        // prompt to confim clearing stored if needed
        // calculate how many traps can be stored

        detonator.storeTrap(trap);

        hero.sprite.operate(cell);
        hero.busy();
        hero.dispelInvisibility();
        hero.onArtifactUsed(detonator);

        hero.spendAndNext(1f);
    }

    @Override
    public List<Integer> availableCells() {
        return range.getCells(context.getHero().getPosition());
    }

    @Override
    public String prompt() {
        return Messages.get(Detonator.class, "store_trap_prompt");
    }
}
