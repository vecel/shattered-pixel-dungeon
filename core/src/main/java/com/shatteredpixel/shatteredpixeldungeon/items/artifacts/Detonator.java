package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns.QuickActivationTalentCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistry;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistryImpl;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.DamageModifier;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapModifierProvider;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapModifierProviderAdapter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/// TODO:
/// 1. Set quickslot action to trap activation
public class Detonator extends Artifact {

    private final TrapModifierProvider trapModifierProvider;
    private final TrapRegistry trapRegistry;

    private final Set<Class<? extends Trap>> activated = new HashSet<>();

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


    public Detonator() {
        super();
        this.trapModifierProvider = new TrapModifierProviderAdapter();
        this.trapRegistry = new TrapRegistryImpl();
    }

    Detonator(GameLogger logger, DungeonInterface dungeon, TrapModifierProvider trapModifierProvider, TrapRegistry trapRegistry) {
        super(logger, dungeon);
        this.trapModifierProvider = trapModifierProvider;
        this.trapRegistry = trapRegistry;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_SET_TRAP);
            actions.add(AC_ACTIVATE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        callExecuteSuper(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (!isEquipped(hero)) {
            logger.info(Messages.get(Artifact.class, "need_to_equip"));
            return;
        }

        if (cursed) {
            logger.warning(Messages.get(Detonator.class, "cursed"));
            return;
        }

        if (action.equals(AC_ACTIVATE)) {
            GameScene.selectCell(activationSelector.init(hero));
        }

        if (action.equals(AC_SET_TRAP)) {
            GameScene.selectCell(setTrapSelector.init(hero));
        }
    }

    private abstract static class DetonatorSelector extends CellSelector.Listener {
        protected Hero hero;
        protected DetonatorSelector init(Hero hero) {
            this.hero = hero;
            return this;
        }
    }

    private final DetonatorSelector activationSelector = new DetonatorSelector() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            if (!hero.withinFieldOfView(cell)) return;

            Trap trap = dungeon.getTrap(cell);
            if (trap == null) {
                logger.info(Messages.get(Detonator.class, "activate_no_trap"));
                return;
            }

            int activateCharge = 1;
            if (getCharge() < activateCharge) {
                logger.info(Messages.get(Detonator.class, "activate_no_charge"));
                return;
            }

            spendCharges(activateCharge);

            DamageModifier modifier = trapModifierProvider.getModifierFor(hero);
            trap.trigger(modifier);

            int exp = trapRegistry.getDanger(trap.getClass());
            if (!activated.contains(trap.getClass())) {
                activated.add(trap.getClass());
                exp += 10;
            }
            gainExp(exp);

            hero.dispelInvisibility();
            hero.onArtifactUsed();

            float time = calculateActivationTime(hero);

            handleLastChargeSpent(hero);
            handleTrapActivation(hero);
            handleQuickActivation(hero);

            hero.spendAndNext(time);
        }

        @Override
        public String prompt() {
            return Messages.get(Detonator.class, "activate_prompt");
        }
    };

    private final DetonatorSelector setTrapSelector = new DetonatorSelector() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            if (!hero.withinFieldOfView(cell)) return;

            if (!dungeon.isCellEmpty(cell)) return;

            int setTrapCharge = 2;
            if (getCharge() < setTrapCharge) {
                logger.info(Messages.get(Detonator.class, "set_trap_no_charge"));
                return;
            }

            spendCharges(setTrapCharge);

            dungeon.setTrap(new WornDartTrap(), cell);

            hero.sprite.operate(cell);
            hero.busy();
            hero.dispelInvisibility();
            hero.onArtifactUsed();

            handleLastChargeSpent(hero);

            hero.spendAndNext(1f);
        }

        @Override
        public String prompt() {
            return Messages.get(Detonator.class, "set_trap_prompt");
        }
    };

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

    @Override
    public String desc() {
        String desc = super.desc();

        if (cursed) return desc + "\n\n" + Messages.get(this, "desc_cursed");

        return desc;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("activated_traps", activated.toArray(new Class[0]));
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (!bundle.contains("activated_traps")) {
            throw new IllegalStateException("Cannot restore Detonator's activated traps, cause there is no field 'activated_traps' in bundle.");
        }
        for (Class<? extends Trap> trap : bundle.getClassArray("activated_traps")) {
            activated.add(trap);
        }
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

    private float calculateActivationTime(Hero hero) {
        if (hero.hasTalent(Talent.QUICK_ACTIVATION) && !hero.hasBuff(QuickActivationTalentCooldown.class)) return 0;
        return 1;
    }

    private void handleLastChargeSpent(Hero hero) {
        if (hasCharges()) return;
        int lastKaboomPoints = hero.pointsInTalent(Talent.LAST_KABOOM);
        if (lastKaboomPoints == 0) return;

        if (lastKaboomPoints >= 1) {
            hero.applyShielding(4);
        }
        if (lastKaboomPoints == 2) {
            hero.applyHealing(2);
        }
    }

    private void handleTrapActivation(Hero hero) {
        if (!hero.hasTalent(Talent.I_CAN_FIGHT_TOO)) return;
        hero.applyBuff(Talent.ICanFightTooTracker.class);
    }

    private void handleQuickActivation(Hero hero) {
        if (!hero.hasTalent(Talent.QUICK_ACTIVATION)) return;
        if (hero.hasBuff(QuickActivationTalentCooldown.class)) return;

        int points = hero.pointsInTalent(Talent.QUICK_ACTIVATION);
        int cooldown = points == 1 ? 50 : 30;

        logger.positive("That was a quick!");

        hero.applyCooldownBuff(new QuickActivationTalentCooldown(), cooldown - 1);
    }

    private void gainExp(int value) {
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

    protected void callExecuteSuper(Hero hero, String action) {
        super.execute(hero, action);
    }
}


