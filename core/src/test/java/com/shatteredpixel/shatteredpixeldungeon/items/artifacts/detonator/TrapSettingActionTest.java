package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogLevel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GdxApplicationExtension.class)
class TrapSettingActionTest {
    private TrapSettingAction action;
    private DetonatorContextFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new DetonatorContextFixture();
        action = new TrapSettingAction(fixture.detonator, fixture.context);
    }
    
    @Test
    void has_correct_prompt() {
        assertEquals(Messages.get(Detonator.class, "set_trap_prompt"), action.prompt());
    }
    
    @Test
    void consumes_charges_when_used() {
        action.execute(1);

        verifyOnce(fixture.detonator).spendCharges(2);
    }
    
    @Test
    void does_not_work_when_cell_is_occupied_by_character() {
        fixture.withCellOccupied();

        action.execute(1);

        verifyNever(fixture.dungeon).setTrap(any(Trap.class), anyInt());
    }

    @Test
    void sets_trap_when_cell_is_occupied_by_flying_character() {
        fixture.withCellOccupiedByFlyingCharacter();

        action.execute(1);

        verifyOnce(fixture.dungeon).setTrap(any(Trap.class), anyInt());
    }
    
    @Test
    void does_not_work_when_cell_is_not_empty() {
        fixture.withSolidCell();

        action.execute(1);

        verifyNever(fixture.dungeon).setTrap(any(Trap.class), anyInt());
    }
    
    @Test
    void works_on_grass_cell() {
        fixture.withGrassCell();

        action.execute(1);

        verifyOnce(fixture.dungeon).setTrap(any(Trap.class), anyInt());
    }
    
    @Test
    void does_not_work_when_out_of_sight() {
        fixture.outOfFieldOfView();

        action.execute(1);

        verifyNever(fixture.dungeon).setTrap(any(Trap.class), anyInt());
    }

    @Test
    void does_not_work_when_out_of_range() {
        LogEntry entry = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "set_trap_out_of_range"));
        fixture.withoutTrapSettingRange();

        action.execute(1);

        verifyNever(fixture.dungeon).setTrap(any(Trap.class), anyInt());
        assertTrue(fixture.logger.contains(entry));
    }

    @Test
    void sets_trap_stored_in_detonator() {
        action.execute(1);

        verifyOnce(fixture.detonator).getStoredTrap();
        verifyOnce(fixture.dungeon).setTrap(fixture.trap, 1);
    }

    @Test
    void triggers_detonator_handlers_when_used() {
        action.execute(1);

        verifyOnce(fixture.hero).dispelInvisibility();
        verifyOnce(fixture.hero).onArtifactUsed(any(ArtifactUsedEvent.class));
        verifyOnce(fixture.hero.sprite).operate(anyInt());
    }

    @Test
    void spends_time_when_used() {
        action.execute(1);

        verifyOnce(fixture.hero).spendAndNext(1f);
    }

    @Test
    void does_not_work_when_no_trap_is_stored() {
        LogEntry entry = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "set_trap_no_store"));
        fixture.withoutStoredTrap();

        action.execute(1);

        assertTrue(fixture.logger.contains(entry));
        verifyNever(fixture.detonator).spendCharges(anyInt());
        verifyNever(fixture.dungeon).setTrap(any(Trap.class), anyInt());
    }
}