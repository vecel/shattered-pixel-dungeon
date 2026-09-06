package com.shatteredpixel.shatteredpixeldungeon.fakes;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.watabou.utils.Reflection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FakeHero extends Hero {
    private final Set<Buff> buffs;
    private final Set<Integer> cellsOutOfFieldOfView;
    private final Map<Talent, Integer> talents;

    private float time = 0;

    public FakeHero() {
        this.buffs = new HashSet<>();
        this.cellsOutOfFieldOfView = new HashSet<>();
        this.talents = new HashMap<>();
    }

    public FakeHero withTalent(Talent talent, int points) {
        talents.put(talent, points);
        return this;
    }

    public FakeHero withBuff(Buff buff) {
        applyBuff(buff.getClass());
        return this;
    }



    @Override
    public void spend(float time) {
        this.time += time;
    }

    @Override
    public void spendAndNext(float time) {
        spend(time);
    }

    @Override
    public void onArtifactUsed(ArtifactUsedEvent event) {
        super.onArtifactUsed(event);
    }

    @Override
    public boolean withinFieldOfView(int cell) {
        return !cellsOutOfFieldOfView.contains(cell);
    }

    @Override
    public void dispelInvisibility() {
        removeBuff(Invisibility.class);
    }

    @Override
    public List<Talent> getTalents() {
        return List.copyOf(talents.keySet());
    }

    @Override
    public boolean hasTalent(Talent talent) {
        return talents.containsKey(talent);
    }

    @Override
    public int pointsInTalent(Talent talent) {
        if (!hasTalent(talent)) return 0;
        return talents.get(talent);
    }

    @Override
    public synchronized <T extends Buff> void applyBuff(Class<T> buffClass) {
        T buff = Reflection.newInstance(buffClass);
        if (buff == null) return;
        if (hasBuff(buffClass)) return;

        buff.target = this;

        buffs.add(buff);
        // add buff to engine
    }

    @Override
    public synchronized <T extends FlavourBuff> void applyCooldownBuff(FlavourBuff buff, float duration) {
        applyBuff(buff.getClass());
    }

    @Override
    public synchronized <T extends Buff> boolean hasBuff(Class<T> buffClass) {
        return buffs
            .stream()
            .anyMatch(buff -> buff.getClass().equals(buffClass));
    }

    private <T extends Buff> void removeBuff(Class<T> buffClass) {
        buffs.removeIf(buff -> buff.getClass().equals(buffClass));
    }

    public float getTime() {
        return time;
    }
}
