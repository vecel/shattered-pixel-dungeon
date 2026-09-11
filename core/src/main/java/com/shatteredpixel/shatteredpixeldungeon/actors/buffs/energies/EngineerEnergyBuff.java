package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.energies;

import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManager;
import com.shatteredpixel.shatteredpixeldungeon.ActiveActionManagerAdapter;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.subclasses.engineer.CrossbowConstruct;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.events.Listener;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class EngineerEnergyBuff extends EnergyBuff
        implements ActionIndicator.Action, Listener<ArtifactUsedEvent> {

    private final ActiveActionManager actionManager;

    public EngineerEnergyBuff() {
        this.actionManager = new ActiveActionManagerAdapter();
    }

    public EngineerEnergyBuff(ActiveActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @Override
    public boolean attachTo(Char target) {
        boolean didAttach = super.attachTo(target);
        if (!didAttach) return false;
        if (!(target instanceof Hero)) return true;

        Hero hero = (Hero) target;
        hero.addListener(ArtifactUsedEvent.class, this);
        return true;
    }

    @Override
    public void detach() {
        super.detach();
        if (!(target instanceof Hero)) return;

        Hero hero = (Hero) target;
        hero.removeListener(this);
    }

    @Override
    public boolean act() {
        if (energy >= 1) actionManager.set(this);
        spend(1f);
        return true;
    }

    @Override
    public void doAction() {

        if (energy < 1) actionManager.clear(this);
    }

    @Override
    public void onEvent(ArtifactUsedEvent event) {
        energy += 1;
    }

    @Override
    public String actionName() {
        return "Craft";
    }

    @Override
    public int indicatorColor() {
        return 0x00FF58;
    }

    @Override
    public int actionIcon() {
        return HeroIcon.ENGINEER_ENERY;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (energy >= 1) actionManager.set(this);
    }

}
