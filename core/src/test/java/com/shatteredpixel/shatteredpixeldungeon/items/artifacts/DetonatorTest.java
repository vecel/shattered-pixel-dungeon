package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TrapRegistry;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.shatteredpixel.shatteredpixeldungeon.utils.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.GameLoggerFake;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.TrapDamageModifierProvider;
import com.watabou.utils.Bundle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class DetonatorTest {

    private Detonator trueDetonator;
    private Detonator detonator;
    private Hero mockHero;
    private DungeonInterface mockDungeon;
    private TrapDamageModifierProvider mockTrapDamageModifierProvider;
    private TrapRegistry mockTrapRegistry;

    private Trap mockTrap;
    private GameLoggerFake loggerFake;

    private final String ACTIVATE = "ACTIVATE";
    private final String SET_TRAP = "SET_TRAP";

    @BeforeEach
    void setUp() {
        mockHero = MockHero.create();
        mockDungeon = mock(DungeonInterface.class);
        mockTrapDamageModifierProvider = mock(TrapDamageModifierProvider.class);
        mockTrapRegistry = mock(TrapRegistry.class);
        mockTrap = mock(Trap.class);
        loggerFake = new GameLoggerFake();

        trueDetonator = new Detonator(loggerFake, mockDungeon, mockTrapDamageModifierProvider, mockTrapRegistry);
        detonator = spy(trueDetonator);

        doReturn(true).when(detonator).isEquipped(mockHero);
        doNothing().when(detonator).callExecuteSuper(any(), any());

        when(mockDungeon.getTrap(any(Integer.class))).thenReturn(mockTrap);
        when(mockDungeon.isCellEmpty(any(Integer.class))).thenReturn(true);
        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(true);
        when(mockTrapDamageModifierProvider.getDamageModifier(mockHero)).thenReturn(Modifier.None);
        when(mockTrapRegistry.getDanger(any(Class.class))).thenReturn(10);

        doNothing().when(mockHero.sprite).operate(1);
    }

    @Test
    void contains_activate_and_set_trap_actions() {
        List<String> result = detonator.actions(mockHero);

        assertTrue(result.contains(ACTIVATE));
        assertTrue(result.contains(SET_TRAP));
    }

    @Test
    void does_not_contain_actions_when_cursed() {
        detonator.cursed = true;

        List<String> result = detonator.actions(mockHero);

        assertFalse(result.contains(ACTIVATE));
        assertFalse(result.contains(SET_TRAP));
    }

    @Test
    void does_not_execute_when_hero_is_immune_to_magic() {
        when(mockHero.hasBuff(MagicImmune.class)).thenReturn(true);

        detonator.execute(mockHero, ACTIVATE);

        verify(detonator, never()).isEquipped(any());
    }

    @Test
    void does_not_execute_and_logs_warning_when_used_cursed() {
        detonator.cursed = true;

        detonator.execute(mockHero, ACTIVATE);

        LogEntry cursedLog = new LogEntry(LogLevel.WARNING, Messages.get(Detonator.class, "cursed"));

        assertEquals(1, loggerFake.getLogs().size());
        assertTrue(loggerFake.contains(cursedLog));
    }

    @Test
    void logs_information_when_used_unequipped() {
        doReturn(false).when(detonator).isEquipped(mockHero);

        detonator.execute(mockHero, ACTIVATE);

        LogEntry unequippedLog = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "need_to_equip"));

        assertEquals(1, loggerFake.getLogs().size());
        assertTrue(loggerFake.getLogs().contains(unequippedLog));
    }

    @Test
    void has_recharging_passive_buff() {
        fail("Not implemented yet");
    }

    @Test
    void recharges_when_equipped() {
        fail("Not implemented");
    }
    
    @Test
    void does_not_charge_when_cursed() {
        detonator.cursed = true;

        detonator.charge(mockHero, 1f);

        verifyNever(detonator).gainCharges(anyInt());
    }

    @Test
    void does_not_charge_when_hero_is_immune_to_magic() {
        when(mockHero.hasBuff(MagicImmune.class)).thenReturn(true);

        detonator.charge(mockHero, 1f);

        verifyNever(detonator).gainCharges(anyInt());
    }

    @Test
    void recharges_in_inventory_when_hero_has_handy_detonator_talent_at_level_one() {
        when(detonator.isUnequipped(mockHero)).thenReturn(true);
        when(mockHero.pointsInTalent(Talent.HANDY_DETONATOR)).thenReturn(1);
        float amount = 1f;
        float expected = 0.25f;

        detonator.charge(mockHero, amount);

        verifyOnce(detonator).gainCharges(expected);
    }

    @Test
    void recharges_in_inventory_when_hero_has_handy_detonator_talent_at_level_two() {
        when(detonator.isUnequipped(mockHero)).thenReturn(true);
        when(mockHero.pointsInTalent(Talent.HANDY_DETONATOR)).thenReturn(2);
        float amount = 1f;
        float expected = 0.50f;

        detonator.charge(mockHero, amount);

        verifyOnce(detonator).gainCharges(expected);
    }

    @Test
    void recharges_in_inventory_with_normal_speed_when_hero_has_handy_detonator_talent_at_level_three() {
        when(detonator.isUnequipped(mockHero)).thenReturn(true);
        when(mockHero.pointsInTalent(Talent.HANDY_DETONATOR)).thenReturn(3);
        float amount = 1f;
        float expected = 1f;

        detonator.charge(mockHero, amount);

        verifyOnce(detonator).gainCharges(expected);
    }

    @Test
    void saves_known_traps_to_bundle() {
        Trap dartTrap = new WornDartTrap();
        Trap gasTrap = new ToxicTrap();

        Bundle bundle = new Bundle();
        detonator.setKnown(dartTrap);
        detonator.setKnown(gasTrap);
        detonator.storeInBundle(bundle);

        Detonator fresh = new Detonator();
        fresh.restoreFromBundle(bundle);

        assertTrue(fresh.isKnown(dartTrap));
        assertTrue(fresh.isKnown(gasTrap));
    }

    @Test
    void saves_stored_trap_to_bundle() {
        Trap gasTrap = new ToxicTrap();

        Bundle bundle = new Bundle();
        detonator.storeTrap(gasTrap);
        detonator.storeInBundle(bundle);

        Detonator fresh = new Detonator();
        fresh.restoreFromBundle(bundle);

        Trap stored = fresh.getStoredTrap();

        assertEquals(gasTrap.getClass(), stored.getClass());
    }
}