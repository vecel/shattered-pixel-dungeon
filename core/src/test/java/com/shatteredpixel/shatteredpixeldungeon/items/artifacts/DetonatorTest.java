package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.fakes.app.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.GameLoggerFake;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.LogLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class DetonatorTest {

    private Detonator trueDetonator;
    private Detonator detonator;
    private Hero mockHero;
    private DungeonInterface mockDungeon;
    private Trap mockTrap;
    private GameLoggerFake loggerFake;

    private final String ACTIVATE = "ACTIVATE";
    private final String SET_TRAP = "SET_TRAP";

    @BeforeEach
    void setUp() {

        mockHero = mock(Hero.class);
        mockDungeon = mock(DungeonInterface.class);
        mockTrap = mock(Trap.class);
        loggerFake = new GameLoggerFake();

        trueDetonator = new Detonator(loggerFake, mockDungeon);
        detonator = spy(trueDetonator);

        doReturn(true).when(detonator).isEquipped(mockHero);
        doNothing().when(detonator).callExecuteSuper(any(), any());

        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(true);
        when(mockDungeon.getTrap(any(Integer.class))).thenReturn(mockTrap);
        when(mockDungeon.isCellEmpty(any(Integer.class))).thenReturn(true);
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
        when(mockHero.buff(MagicImmune.class)).thenReturn(mock(MagicImmune.class));

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
    void activation_prompt_is_correct() {
        CellSelector.Listener listener = captureListener(ACTIVATE);

        String prompt = Messages.get(Detonator.class, "activate_prompt");
        assertEquals(prompt, listener.prompt());
    }

    @Test
    void activation_consumes_two_charges() {
        // Listener operates on REAL detonator, not spied one
        trueDetonator.charge = 5;

        CellSelector.Listener listener = captureListener(ACTIVATE);
        listener.onSelect(1);

        assertEquals(3, trueDetonator.charge);
        verify(mockTrap, times(1)).activateAndDisarm();
        verify(mockHero, times(1)).dispelInvisibility();
        verify(mockHero, times(1)).onArtifactUsed();
        verify(mockHero, times(1)).spendAndNext(1f);
    }

    @Test
    void does_not_activate_and_log_message__when_charge_is_too_low() {
        trueDetonator.charge = 1;

        CellSelector.Listener listener = captureListener(ACTIVATE);
        listener.onSelect(1);

        LogEntry noChargeLog = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "activate_no_charge"));

        assertTrue(loggerFake.contains(noChargeLog));
        verify(mockTrap, never()).activateAndDisarm();
    }

    @Test
    void does_not_activate_when_no_trap_is_targeted() {
        when(mockDungeon.getTrap(1)).thenReturn(null);

        CellSelector.Listener listener = captureListener(ACTIVATE);
        listener.onSelect(1);

        LogEntry noTrapLog = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "activate_no_trap"));

        assertTrue(loggerFake.contains(noTrapLog));
        verify(mockTrap, never()).activateAndDisarm();
    }

    @Test
    void does_not_activate_when_cell_is_not_in_field_of_view() {
        when(mockHero.withinFieldOfView(1)).thenReturn(false);

        CellSelector.Listener listener = captureListener(ACTIVATE);
        listener.onSelect(1);

        verify(mockDungeon, never()).getTrap(1);
    }

    @Test
    void set_trap_prompt_is_correct() {
        CellSelector.Listener listener = captureListener(SET_TRAP);

        String prompt = Messages.get(Detonator.class, "set_trap_prompt");
        assertEquals(prompt, listener.prompt());
    }

    @Test
    void setting_trap_consumes_one_charge() {
        CellSelector.Listener listener = captureListener(SET_TRAP);
        trueDetonator.charge = 5;

        listener.onSelect(1);

        assertEquals(4, trueDetonator.charge);
        verify(mockHero, times(1)).dispelInvisibility();
        verify(mockHero, times(1)).onArtifactUsed();
        verify(mockHero, times(1)).spendAndNext(1f);
    }

    @Test
    void does_not_set_trap_and_logs_message_when_has_no_charges() {
        CellSelector.Listener listener = captureListener(SET_TRAP);
        trueDetonator.charge = 0;

        listener.onSelect(1);

        LogEntry noChargeLog = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "set_trap_no_charge"));

        assertTrue(loggerFake.contains(noChargeLog));
        verify(mockDungeon, never()).setTrap(any(Trap.class), any(Integer.class));
    }

    @Test
    void does_not_set_trap_when_cell_is_not_in_field_of_view() {
        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(false);

        CellSelector.Listener listener = captureListener(SET_TRAP);
        listener.onSelect(1);

        verify(mockDungeon, never()).isCellEmpty(any(Integer.class));
        verify(mockDungeon, never()).setTrap(any(Trap.class), any(Integer.class));
    }

    @Test
    void does_not_set_trap_when_cell_is_not_empty() {
        when(mockDungeon.isCellEmpty(any(Integer.class))).thenReturn(false);

        CellSelector.Listener listener = captureListener(SET_TRAP);
        listener.onSelect(1);

        verify(mockDungeon, never()).setTrap(any(Trap.class), any(Integer.class));
    }

    @Test
    void has_recharging_passive_buff() {
        fail("Not implemented yet");
    }

    private CellSelector.Listener captureListener(String action) {
        ArgumentCaptor<CellSelector.Listener> captor = ArgumentCaptor.forClass(CellSelector.Listener.class);
        CellSelector.Listener listener;

        try (MockedStatic<GameScene> mockScene = mockStatic(GameScene.class)) {
            detonator.execute(mockHero, action);
            mockScene.verify(() -> GameScene.selectCell(captor.capture()));
            listener = captor.getValue();
            assertNotNull(listener);
        }

        return listener;
    }
}