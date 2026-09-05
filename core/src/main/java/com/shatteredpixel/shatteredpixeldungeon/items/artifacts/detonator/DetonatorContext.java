package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistry;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapDamageModifierProvider;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

public class DetonatorContext {
    private final Hero hero;
    private final DungeonInterface dungeon;
    private final GameLogger logger;
    private final TrapRegistry registry;
    private final TrapDamageModifierProvider trapDamageModifierProvider;

    public DetonatorContext(Hero hero, DungeonInterface dungeon, GameLogger logger, TrapRegistry registry, TrapDamageModifierProvider trapDamageModifierProvider) {
        this.hero = hero;
        this.dungeon = dungeon;
        this.logger = logger;
        this.registry = registry;
        this.trapDamageModifierProvider = trapDamageModifierProvider;
    }

    public Hero getHero() {
        return hero;
    }

    public DungeonInterface getDungeon() {
        return dungeon;
    }

    public GameLogger getLogger() {
        return logger;
    }

    public TrapRegistry getRegistry() {
        return registry;
    }

    public TrapDamageModifierProvider getTrapModifierProvider() {
        return trapDamageModifierProvider;
    }
}
