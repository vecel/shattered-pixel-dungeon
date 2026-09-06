package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.cooldowns.QuickActivationTalentCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.fakes.FakeHero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistry;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapDamageModifierProvider;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.GameLoggerFake;

public class DetonatorContextFixture {
    public Hero hero = MockHero.create();
    public final DungeonInterface dungeon = mock(DungeonInterface.class);
    public final GameLoggerFake logger = new GameLoggerFake();
    public final TrapRegistry registry = mock(TrapRegistry.class);
    public final TrapDamageModifierProvider provider = mock(TrapDamageModifierProvider.class);
    public final Detonator detonator = spy(Detonator.class);
    public final Trap trap = mock(Trap.class);

    public DetonatorContext context = new DetonatorContext(
            hero, dungeon, logger, registry, provider
    );

    /**
     * @deprecated Use {@link DetonatorContextFixture(Hero)} constructor.
     */
    @Deprecated
    public DetonatorContextFixture() {
        when(hero.withinFieldOfView(anyInt())).thenReturn(true);
        initializeMocks();
    }

    private void initializeMocks() {
        when(dungeon.getTrap(anyInt())).thenReturn(trap);
        when(dungeon.isCellEmpty(anyInt())).thenReturn(true);
        when(dungeon.hasVisibleTrapAt(anyInt())).thenReturn(true);
        when(provider.getDamageModifier(hero)).thenReturn(Modifier.None);
        when(detonator.isKnown(trap)).thenReturn(true);
        when(trap.isActive()).thenReturn(true);

        detonator.setCharge(10);

        doNothing().when(trap).trigger(any(Modifier.class));
        doNothing().when(detonator).spendCharges(anyInt());
        doReturn(trap).when(detonator).getStoredTrap();
        doReturn(true).when(detonator).hasStoredTrap();
    }

    public void withoutTrap() {
        when(dungeon.getTrap(anyInt())).thenReturn(null);
    }

    public void withTrapDanger(int danger) {
        when(registry.getDanger(any(Class.class))).thenReturn(danger);
    }

    public void withCharge(int charge) {
        detonator.setCharge(charge);
    }

    public void withQuickActivationTalent() {
        when(hero.hasTalent(Talent.QUICK_ACTIVATION)).thenReturn(true);
        when(hero.pointsInTalent(Talent.QUICK_ACTIVATION)).thenReturn(1);
        when(hero.hasBuff(QuickActivationTalentCooldown.class)).thenReturn(false);
    }

    public void withTrapUnknown() {
        doReturn(false).when(detonator).isKnown(any(Trap.class));
    }

    public void outOfFieldOfView() {
        when(hero.withinFieldOfView(anyInt())).thenReturn(false);
    }

    public void withGrassCell() {
        when(dungeon.isCellEmpty(anyInt())).thenReturn(false);
        when(dungeon.isCellGrass(anyInt())).thenReturn(true);
    }

    public void withSolidCell() {
        when(dungeon.isCellEmpty(anyInt())).thenReturn(false);
        when(dungeon.isCellGrass(anyInt())).thenReturn(false);
        when(dungeon.isCellEmbers(anyInt())).thenReturn(false);
    }

    public void withCellOccupied() {
        when(dungeon.isCellOccupied(anyInt())).thenReturn(true);
    }

    public void withCellOccupiedByFlyingCharacter() {
        when(dungeon.isCellOccupiedByFlyingCharacter(anyInt())).thenReturn(true);
    }

    public void withInactiveTrap() {
        when(trap.isActive()).thenReturn(false);
    }

    public void withoutStoredTrap() {
        doReturn(null).when(detonator).getStoredTrap();
        doReturn(false).when(detonator).hasStoredTrap();
    }
}
