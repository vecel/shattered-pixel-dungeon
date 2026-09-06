package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.talents.ArtifactUsedEvent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.modifiers.Modifier;
import com.shatteredpixel.shatteredpixeldungeon.utils.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogLevel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @deprecated This test class uses deprecated mocked Hero. Add new tests in {@link TrapActivationActionWithHeroFakeTest}.
 */
@Deprecated
@ExtendWith(GdxApplicationExtension.class)
class TrapActivationActionTest {

    private TrapActivationAction action;
    private DetonatorContextFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new DetonatorContextFixture();
        action = new TrapActivationAction(fixture.detonator, fixture.context);
    }
    
    @Test
    void has_correct_prompt() {
        assertEquals(Messages.get(Detonator.class, "activate_trap_prompt"), action.prompt());
    }
    
    @Test
    void consumes_charge_when_used() {
        int toSpend = 1;

        action.execute(1);

        verifyOnce(fixture.detonator).spendCharges(toSpend);
    }
    
    @Test
    void does_not_activate_when_there_is_no_trap() {
        fixture.withoutTrap();
        LogEntry entry = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "activate_no_trap"));

        action.execute(1);

        verifyNever(fixture.detonator).spendCharges(anyInt());
        assertTrue(fixture.logger.contains(entry));
    }
    
    @Test
    void does_not_activate_when_out_of_sight() {
        fixture.outOfFieldOfView();

        action.execute(1);

        verifyNever(fixture.dungeon).getTrap(anyInt());
    }

    @Test
    void does_not_activate_when_out_of_charge() {
        fixture.withCharge(0);
        LogEntry entry = new LogEntry(LogLevel.INFO, Messages.get(Detonator.class, "activate_no_charge"));

        action.execute(1);

        verifyNever(fixture.trap).trigger(any(Modifier.class));
        assertTrue(fixture.logger.contains(entry));
    }

    @Test
    void triggers_trap_when_used() {
        action.execute(1);

        verifyOnce(fixture.trap).trigger(any(Modifier.class));
    }

    @Test
    void gives_detonator_exp_equals_to_trap_danger_when_used_and_trap_is_known() {
        int danger = 10;
        fixture.withTrapDanger(danger);

        action.execute(1);

        verifyOnce(fixture.detonator).gainExp(danger);
    }
    
    @Test
    void gives_detonator_extra_exp_when_trap_is_not_known() {
        int danger = 30;
        int extra = 10;
        fixture.withTrapUnknown();
        fixture.withTrapDanger(danger);

        action.execute(1);

        verifyOnce(fixture.detonator).gainExp(danger + extra);
        verifyOnce(fixture.detonator).setKnown(fixture.trap);
    }

    @Test
    void triggers_handlers_when_used() {
        fixture.withCharge(1);

        action.execute(1);

        verifyOnce(fixture.hero).dispelInvisibility();
        verifyOnce(fixture.hero).onArtifactUsed(any(ArtifactUsedEvent.class));
    }

    @Test
    void spends_time_when_used() {
        action.execute(1);

        verifyOnce(fixture.hero).spendAndNext(1f);
    }

    @Test
    void spends_no_time_when_used_with_quick_activation_talent() {
        fixture.withQuickActivationTalent();

        action.execute(1);

        verifyOnce(fixture.hero).spendAndNext(0f);
        verifyNever(fixture.hero).spendAndNext(1f);
    }
}