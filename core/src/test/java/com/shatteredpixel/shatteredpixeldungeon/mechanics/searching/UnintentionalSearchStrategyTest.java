package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.fakes.app.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.fakes.hero.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.GameLoggerFake;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.LogLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class UnintentionalSearchStrategyTest {
    private UnintentionalSearchStrategy strategy;
    private SearchContextFixture context;
    private Shape mockTrapShape;
    private Shape mockDoorShape;
    private Hero mockHero;
    private Trap mockTrap;

    @BeforeEach
    void setUp() {
        context = new SearchContextFixture();

        mockHero = MockHero.create();
        mockTrapShape = mock(Shape.class);
        mockDoorShape = mock(Shape.class);
        mockTrap = mock(Trap.class);

        strategy = new UnintentionalSearchStrategy(context.mockDungeon, context.fakeLogger, context.mockScene, context.mockAudio, mockTrapShape, mockDoorShape);

        when(mockTrapShape.getCells(any(Integer.class))).thenReturn(List.of(1));
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of(2));
        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(true);
        when(context.mockDungeon.getTrap(any(Integer.class))).thenReturn(mockTrap);
    }

    @Test
    void does_not_search_on_hero_position() {
        mockHero.pos = 1;

        strategy.execute(mockHero);

        verifyNever(context.mockDungeon).getCell(any(Integer.class));
    }

    @Test
    void does_not_search_when_cell_is_out_of_field_of_view() {
        when(mockHero.withinFieldOfView(1)).thenReturn(false);

        strategy.execute(mockHero);

        verifyNever(context.mockDungeon).getCell(any(Integer.class));
    }

    @Test
    void stops_searching_if_there_is_no_secret() {
        when(context.mockDungeon.hasSecretAt(any(Integer.class))).thenReturn(false);

        strategy.execute(mockHero);

        verifyNever(context.mockDungeon).getCell(any(Integer.class));
    }

    @Test
    void discovers_a_trap_when_roll_is_good() {
        fail("Not implemented");
    }

    @Test
    void discovers_a_trap_when_roll_is_bad() {
        fail("Not implemented");
    }

    @Test
    void has_higher_chance_to_detect_a_trap_with_trap_sense_talent() {
        fail("Not implemented");
    }

    @Test
    void does_check_separate_shapes_for_trap_and_door() {
        fail("Not implemented");
    }

    @Test
    void logs_and_plays_audio_when_hero_did_found_anything() {
        strategy.trapsFound = 1;

        strategy.executePostSearchAction(mockHero);

        LogEntry log = new LogEntry(LogLevel.WARNING, Messages.get(Hero.class, "noticed_smth"));

        assertTrue(context.fakeLogger.contains(log));
        verifyOnce(context.mockAudio).play(Assets.Sounds.SECRET);
        verifyOnce(mockHero).interrupt();
    }
}