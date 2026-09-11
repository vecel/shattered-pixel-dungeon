package com.shatteredpixel.shatteredpixeldungeon.fakes;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.events.GameEvent;
import com.shatteredpixel.shatteredpixeldungeon.events.Listener;
import com.watabou.utils.Reflection;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeroFake extends Hero {
    private final Set<Buff> buffs;
    private final Set<Integer> cellsOutOfFieldOfView;
    private final Map<Talent, Integer> talents;
    private final Set<Listener<? extends GameEvent>> listeners;

    private float time = 0;

    public HeroFake() {
        this.buffs = new HashSet<>();
        this.cellsOutOfFieldOfView = new HashSet<>();
        this.talents = new HashMap<>();
        this.listeners = new HashSet<>();
    }

    public HeroFake withTalent(Talent talent, int points) {
        talents.put(talent, points);
        return this;
    }

    public HeroFake withBuff(Buff buff) {
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
        remove(Invisibility.class);
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
    public boolean add(Buff buff) {
        buff.target = this;
        buffs.add(buff);
        return true;
    }

    @Override
    public boolean remove(Buff buff) {
        buffs.remove(buff);
        return true;
    }

    @Override
    public synchronized <T extends Buff> void applyBuff(Class<T> buffClass) {
        T buff = Reflection.newInstance(buffClass);
        if (buff == null) return;
        if (hasBuff(buffClass)) return;
        add(buff);
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

    @SuppressWarnings("CheckResult")
    @Override
    public synchronized <T extends Buff> @Nullable T getBuff(Class<T> buffClass) {
        return buffs.stream()
            .filter(buff -> buff.getClass().equals(buffClass))
            .map(buffClass::cast)
            .findFirst()
            .orElse(null);
    }

    @Override
    public <T extends GameEvent> void addListener(Class<T> type, Listener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public <T extends GameEvent> void removeListener(Listener<T> listener) {
        listeners.remove(listener);
    }

    public boolean hasListener(Listener<?> listener) {
        return listeners.contains(listener);
    }

    public float getTime() {
        return time;
    }
}
