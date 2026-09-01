package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistry;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ActionTimeCalculator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.DamageModifier;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapModifierProvider;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

import java.util.List;

public class TrapActivationAction extends DetonatorAction {

    private final ActionTimeCalculator actionTime;
    public TrapActivationAction(Detonator detonator, DetonatorContext context) {
        super(detonator, context);
        this.actionTime = new TrapActivationTimeCalculator();
    }

    @Override
    public void execute(int cell) {
        Hero hero = context.getHero();
        DungeonInterface dungeon = context.getDungeon();
        GameLogger logger = context.getLogger();
        TrapModifierProvider provider = context.getTrapModifierProvider();
        TrapRegistry registry = context.getRegistry();

        if (!hero.withinFieldOfView(cell)) return;

        Trap trap = dungeon.getTrap(cell);
        if (trap == null) {
            logger.info(Messages.get(Detonator.class, "activate_no_trap"));
            return;
        }

        int activateCharge = 1;
        if (detonator.getCharge() < activateCharge) {
            logger.info(Messages.get(Detonator.class, "activate_no_charge"));
            return;
        }

        detonator.spendCharges(activateCharge);

        DamageModifier modifier = provider.getModifierFor(hero);
        trap.trigger(modifier);

        int exp = registry.getDanger(trap.getClass());
        if (!detonator.isKnown(trap)) {
            detonator.setKnown(trap);
            exp += 10;
        }
        detonator.gainExp(exp);

        hero.dispelInvisibility();

        ArtifactUsedEvent event = new ArtifactUsedEvent(hero, detonator, Detonator.AC_ACTIVATE);
        hero.onArtifactUsed(event);

        float time = actionTime.calculate(hero);

        hero.spendAndNext(time);
    }

    @Override
    public List<Integer> availableCells() {
        return List.of();
    }

    @Override
    public String prompt() {
        return Messages.get(Detonator.class, "activate_trap_prompt");
    }
}
