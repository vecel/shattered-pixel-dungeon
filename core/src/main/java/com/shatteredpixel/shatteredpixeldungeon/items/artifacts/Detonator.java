package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

import java.util.ArrayList;

public class Detonator extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_DETONATOR;

        exp = 0;
        levelCap = 10;

        charge = Math.min(level()+3, 10);
        partialCharge = 0;
        chargeCap = Math.min(level()+3, 10);

        unique = true;
    }

    public static final String AC_ACTIVATE = "ACTIVATE";
    public static final String AC_SET_TRAP = "SET_TRAP";


    public Detonator() {}

    Detonator(GameLogger logger, DungeonInterface dungeon) {
        super(logger, dungeon);
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

            int ACTIVATE_CHARGE = 1;
            if (getCharge() < ACTIVATE_CHARGE) {
                logger.info(Messages.get(Detonator.class, "activate_no_charge"));
                return;
            }

            spendCharges(ACTIVATE_CHARGE);

            trap.trigger();

            hero.dispelInvisibility();
            hero.onArtifactUsed();

            handleLastChargeSpent(hero);
            handleTrapActivation(hero);

            hero.spendAndNext(1f);
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

            int SET_TRAP_CHARGE = 2;
            if (getCharge() < SET_TRAP_CHARGE) {
                logger.info(Messages.get(Detonator.class, "set_trap_no_charge"));
                return;
            }

            spendCharges(SET_TRAP_CHARGE);

            // TODO: change trap type
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
    protected ArtifactBuff passiveBuff() {
        return new DetonatorRecharge();
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (cursed) return desc + "\n\n" + Messages.get(this, "desc_cursed");

        return desc;
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

    protected void callExecuteSuper(Hero hero, String action) {
        super.execute(hero, action);
    }
}
