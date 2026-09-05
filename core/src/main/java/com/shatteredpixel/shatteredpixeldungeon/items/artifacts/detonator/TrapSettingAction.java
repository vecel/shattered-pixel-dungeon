package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.karandys.todo.Todo;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ChargeUsageCalculator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.CircularShape;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.SquareShape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

import java.util.List;
import java.util.stream.Collectors;

public class TrapSettingAction extends DetonatorAction {

    private final ActionTimeCalculator actionTime;
    private final ChargeUsageCalculator chargeUsage;

    public TrapSettingAction(Detonator detonator, DetonatorContext context) {
        super(detonator, context);
        this.actionTime = new TrapSettingTimeCalculator();
        this.chargeUsage = new TrapSettingChargeCalculator();
    }

    @Override
    public void execute(int cell) {
        Hero hero = context.getHero();
        DungeonInterface dungeon = context.getDungeon();
        GameLogger logger = context.getLogger();

        if (!hero.withinFieldOfView(cell)) return;

        if (!detonator.hasStoredTrap()) {
            logger.info(Messages.get(Detonator.class, "set_trap_no_store"));
            return;
        }

        if (!isCellAvailable(cell)) {
            logger.info(Messages.get(Detonator.class, "set_trap_out_of_range"));
            return;
        }

        if (dungeon.isCellOccupied(cell) && !dungeon.isCellOccupiedByFlyingCharacter(cell)) return;

        int setTrapCharge = chargeUsage.calculate(hero);
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

        ArtifactUsedEvent event = new ArtifactUsedEvent(hero, detonator, Detonator.AC_SET_TRAP);
        hero.onArtifactUsed(event);

        float time = actionTime.calculate(hero);

        hero.spendAndNext(time);
    }

    @Override
    public List<Integer> availableCells() {
        Hero hero = context.getHero();
        Shape shape = getActionRange();

        return shape.getCells(hero.getPosition())
            .stream()
            .filter(this::isCellAvailable)
            .filter(cell -> cell != hero.getPosition())
            .collect(Collectors.toList());
    }

    @Override
    public String prompt() {
        return Messages.get(Detonator.class, "set_trap_prompt");
    }

    private boolean isCellAvailable(int cell) {
        DungeonInterface dungeon = context.getDungeon();
        return dungeon.isCellEmpty(cell) || dungeon.isCellGrass(cell) || dungeon.isCellEmbers(cell);
    }

    protected Shape getActionRange() {
        Hero hero = context.getHero();

        int points = hero.pointsInTalent(Talent.DETONATOR_RANGE);
        int radius = 1 + points;

        return new CircularShape(radius);
    }
}
