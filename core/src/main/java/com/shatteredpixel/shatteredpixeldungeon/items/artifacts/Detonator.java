package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

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

    public static final String AC_DETONATE = "DETONATE";
    public static final String AC_SET_TRAP = "SET_TRAP";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_SET_TRAP);
            actions.add(AC_DETONATE);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (!isEquipped(hero)) {
            GLog.i(Messages.get(Artifact.class, "need_to_equip"));
            return;
        }
        if (cursed) {
            GLog.i( Messages.get(this, "cursed") );
            return;
        }

        if (action.equals(AC_DETONATE)) {
            GameScene.selectCell(activationSelector);
        }

        if (action.equals(AC_SET_TRAP)) {
            GameScene.selectCell(setTrapSelector);
        }
    }

    private final CellSelector.Listener activationSelector = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            Hero hero = Dungeon.hero;
            if (!hero.withinFieldOfView(cell)) return;

            Trap trap = Dungeon.getTrap(cell);
            if (trap == null) {
                GLog.i(Messages.get(Detonator.class, "activate_no_trap"));
                return;
            }

            if (charge <= 1) {
                GLog.i(Messages.get(Detonator.class, "activate_no_charge"));
                return;
            }

            charge -= 2;
            trap.activateAndDisarm();

            hero.dispelInvisibility();
            hero.onArtifactUsed();
            hero.spendAndNext(1f);
        }

        @Override
        public String prompt() {
            return Messages.get(Detonator.class, "activate_prompt");
        }
    };

    private final CellSelector.Listener setTrapSelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            if (!Dungeon.isCellEmpty(cell)) return;

            if (charge <= 0) {
                GLog.i(Messages.get(Detonator.class, "set_trap_no_charge"));
                return;
            }

            charge -= 1;

            // TODO: change trap type
            Dungeon.setTrap(new WornDartTrap(), cell);

            Hero hero = Dungeon.hero;

            hero.dispelInvisibility();
            hero.onArtifactUsed();
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
                    if (!isEquipped(Dungeon.hero)){
                        chargeToGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
                    }
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
}
