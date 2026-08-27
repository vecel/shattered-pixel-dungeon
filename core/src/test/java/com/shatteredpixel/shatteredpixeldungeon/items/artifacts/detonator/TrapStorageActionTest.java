package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.detonator;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Detonator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.SquareShape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GdxApplicationExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class TrapStorageActionTest {

    private TrapStorageAction action;
    private DetonatorContextFixture fixture;
    private final Shape range = mock(Shape.class);

    @BeforeEach
    void setUp() {
        fixture = new DetonatorContextFixture();
        action = new TrapStorageAction(fixture.detonator, fixture.context, range);

        when(range.getCells(anyInt())).thenReturn(List.of(1, 2, 3));
    }
    
    @Test
    void has_correct_prompt() {
        assertEquals(Messages.get(Detonator.class, "store_trap_prompt"), action.prompt());
    }
    
    @Test
    void can_be_used_on_active_trap_only() {
        fixture.withInactiveTrap();

        action.execute(1);

        verifyNever(fixture.detonator).storeTrap(fixture.trap);
    }

    @Test
    void can_be_used_on_adjacent_trap_only() {
        assertEquals(List.of(1, 2, 3), action.availableCells());
    }
    
    @Test
    void does_not_consume_charge_when_used() {
        action.execute(1);

        verifyNever(fixture.detonator).spendCharges(anyInt());
    }
    
    @Test
    void spends_time_when_used() {
        action.execute(1);

        verifyOnce(fixture.hero).spendAndNext(1f);
    }

    @Test
    void stores_a_trap() {
        action.execute(1);

        verifyOnce(fixture.hero).busy();
        verifyOnce(fixture.hero).onArtifactUsed();
        verifyOnce(fixture.hero).dispelInvisibility();
        verifyOnce(fixture.hero.sprite).operate(anyInt());
    }
}