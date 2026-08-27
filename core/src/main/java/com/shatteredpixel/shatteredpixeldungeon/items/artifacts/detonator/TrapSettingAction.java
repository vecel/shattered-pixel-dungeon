package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

import java.util.List;

public class TrapSettingAction extends DetonatorAction {
    public TrapSettingAction(Detonator detonator, DetonatorContext context) {
        super(detonator, context);
    }

    @Override
    public void execute(int cell) {
        Hero hero = context.getHero();
        DungeonInterface dungeon = context.getDungeon();
        GameLogger logger = context.getLogger();

        if (!hero.withinFieldOfView(cell)) return;
        if (!dungeon.isCellEmpty(cell) && !dungeon.isCellGrass(cell)) return;

        if (!detonator.hasStoredTrap()) {
            logger.info(Messages.get(Detonator.class, "set_trap_no_store"));
            return;
        }

        if (!detonator.isCellWithinTrapSettingRange(cell, hero)) {
            logger.info(Messages.get(Detonator.class, "set_trap_out_of_range"));
            return;
        }

        if (dungeon.isCellOccupied(cell) && !dungeon.isCellOccupiedByFlyingCharacter(cell)) return;

        int setTrapCharge = 2;
        if (detonator.getCharge() < setTrapCharge) {
            logger.info(Messages.get(Detonator.class, "set_trap_no_charge"));
            return;
        }


        Trap newTrap = detonator.getStoredTrap();
        dungeon.setTrap(newTrap, cell);

        detonator.spendCharges(setTrapCharge);

        hero.sprite.operate(cell);
        hero.busy();
        hero.dispelInvisibility();
        hero.onArtifactUsed();

        detonator.handleLastChargeSpent(hero);

        hero.spendAndNext(1f);
    }

    @Override
    public List<Integer> availableCells() {
        return List.of();
    }

    @Override
    public String prompt() {
        return Messages.get(Detonator.class, "set_trap_prompt");
    }
}
