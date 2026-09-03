package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.karandys.todo.Todo;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator.DetonatorAction;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator.TrapSettingAction;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator.TrapActivationAction;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator.DetonatorContext;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator.TrapStorageAction;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistry;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistryImpl;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.CircularShape;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapModifierProvider;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapModifierProviderAdapter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneAdapter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Detonator extends Artifact {

    private final TrapModifierProvider trapModifierProvider;
    private final TrapRegistry trapRegistry;
    private final GameSceneInterface scene;

    private final Set<Class<? extends Trap>> knownTraps = new HashSet<>();
    private final List<Class<? extends Trap>> storedTraps = new ArrayList<>();
    private boolean actionConsumedCharge = false;

    {
        image = ItemSpriteSheet.ARTIFACT_DETONATOR;

        exp = 0;
        levelCap = 10;

        charge = Math.min(level()+3, 10);
        partialCharge = 0;
        chargeCap = Math.min(level()+3, 10);

        unique = true;
        defaultAction = AC_ACTIVATE;
    }

    public static final String AC_ACTIVATE = "ACTIVATE";
    public static final String AC_SET_TRAP = "SET_TRAP";
    public static final String AC_STORE_TRAP = "STORE_TRAP";

    public Detonator() {
        super();
        this.trapModifierProvider = new TrapModifierProviderAdapter();
        this.trapRegistry = new TrapRegistryImpl();
        this.scene = new GameSceneAdapter();
    }

    @Todo("Pass scene as dependency")
    Detonator(GameLogger logger, DungeonInterface dungeon, TrapModifierProvider trapModifierProvider, TrapRegistry trapRegistry) {
        super(logger, dungeon);
        this.trapModifierProvider = trapModifierProvider;
        this.trapRegistry = trapRegistry;

        this.scene = new GameSceneAdapter();
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && !cursed && !hero.hasBuff(MagicImmune.class)) {
            actions.add(AC_STORE_TRAP);
            actions.add(AC_SET_TRAP);
            actions.add(AC_ACTIVATE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        callExecuteSuper(hero, action);

        if (hero.hasBuff(MagicImmune.class)) return;

        if (!isEquipped(hero)) {
            logger.info(Messages.get(Artifact.class, "need_to_equip"));
            return;
        }

        if (cursed) {
            logger.warning(Messages.get(Detonator.class, "cursed"));
            return;
        }

        actionConsumedCharge = false;
        DetonatorContext context = new DetonatorContext(hero, dungeon, logger, trapRegistry,
            trapModifierProvider);

        DetonatorAction strategy;

        if (action.equals(AC_ACTIVATE)) {
            strategy = new TrapActivationAction(this, context);
            applyAction(strategy);
        }

        if (action.equals(AC_SET_TRAP)) {
            strategy = new TrapSettingAction(this, context);
            applyAction(strategy);
        }

        if (action.equals(AC_STORE_TRAP)) {
            strategy = new TrapStorageAction(this, context);
            applyAction(strategy);
        }
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.hasBuff(MagicImmune.class)) return;

        gainCharges(amount);
        updateQuickslot();
    }

    @Override
    public Item upgrade() {
        chargeCap = Math.min(chargeCap + 1, 10);
        logger.positive(Messages.get(Detonator.class, "level_up"));
        return super.upgrade();
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new DetonatorRecharge();
    }

    @Todo("Add stored trap name")
    @Override
    public String desc() {
        String desc = super.desc();

        if (cursed) return desc + "\n\n" + Messages.get(this, "desc_cursed");
        if (!hasStoredTrap()) return desc + "\n\n" + Messages.get(this, "desc_no_store");

        return desc + "\n\n" + Messages.get(this, "desc_stored", getStoredTrap().getClass().getName());
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("activated_traps", knownTraps.toArray(new Class[0]));
        bundle.put("stored_trap", storedTraps.toArray(new Class[0]));
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (!bundle.contains("activated_traps")) {
            throw new IllegalStateException("Cannot restore Detonator's activated traps, cause there is no field 'activated_traps' in bundle.");
        }
        if (!bundle.contains("stored_trap")) {
            throw new IllegalStateException("Cannot restore Detonator's stored trap, cause there is no field 'stored_trap' in bundle.");
        }
        for (Class<? extends Trap> trap : bundle.getClassArray("activated_traps")) {
            knownTraps.add(trap);
        }
        for (Class<? extends Trap> trap : bundle.getClassArray("stored_trap")) {
            storedTraps.add(trap);
        }
    }

    @Override
    public void spendCharges(int value) {
        super.spendCharges(value);
        actionConsumedCharge = true;
    }

    public class DetonatorRecharge extends ArtifactBuff {
        @Override
        public boolean act() {
            if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
                if (Regeneration.regenOn()) {
                    float missing = (chargeCap - charge);
                    if (level() > 7) missing += 5*(level() - 7)/3f;
                    float turnsToCharge = (45 - missing);
                    turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
                    float chargeToGain = (1f / turnsToCharge);
                    partialCharge += chargeToGain;
                }

                while (partialCharge >= 1) {
                    charge++;
                    partialCharge -= 1;
                    if (charge == chargeCap){
                        partialCharge = 0;
                    }

                }
            } else {
                partialCharge = 0;
            }

            if (cooldown > 0)
                cooldown --;

            updateQuickslot();

            spend( TICK );

            return true;
        }

    }

    public void storeTrap(Trap trap) {
        storedTraps.clear();
        storedTraps.add(trap.getClass());
    }

    public Trap getStoredTrap() {
        return trapRegistry.create(storedTraps.get(0));
    }

    public boolean hasStoredTrap() {
        return !storedTraps.isEmpty();
    }

    public void setKnown(Trap trap) {
        knownTraps.add(trap.getClass());
    }

    public boolean isKnown(Trap trap) {
        return knownTraps.contains(trap.getClass());
    }

    public boolean didSpendCharge() {
        return actionConsumedCharge;
    }

    private void applyAction(DetonatorAction strategy) {
        scene.highlight(strategy.availableCells());

        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell == null) return;
                strategy.execute(cell);
            }

            @Override
            public void onCancel() {
                scene.cancelHighlight();
            }

            @Override
            public String prompt() {
                return strategy.prompt();
            }
        });
    }


    public void gainExp(int value) {
        if (level() == levelCap) return;

        exp += value;
        int expToLevelUp = calculateLevelUpExp();
        while (exp > expToLevelUp) {
            exp -= expToLevelUp;
            upgrade();
            expToLevelUp = calculateLevelUpExp();
        }

        updateQuickslot();
    }

    private int calculateLevelUpExp() {
        return Math.min(10 * level() * level() + 50, 500);
    }

    @Deprecated
    public boolean isCellWithinTrapSettingRange(int cell, Hero hero) {
        int points = hero.pointsInTalent(Talent.DETONATOR_RANGE);
        int radius = 1 + points;

        int center = hero.getPosition();
        Shape shape = new CircularShape(radius);
        return shape.getCells(center).contains(cell);
    }

    protected void callExecuteSuper(Hero hero, String action) {
        super.execute(hero, action);
    }
}


