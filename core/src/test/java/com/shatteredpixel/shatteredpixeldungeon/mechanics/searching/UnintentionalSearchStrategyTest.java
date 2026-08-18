package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyNever;
import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.fakes.app.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.fakes.hero.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.fakes.logger.LogLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class UnintentionalSearchStrategyTest {
    private UnintentionalSearchStrategy strategy;
    private SearchContextFixture context;
    private SearchStrategyTestVerifier verifier;
    private Shape mockTrapShape;
    private Shape mockDoorShape;
    private Hero mockHero;
    private Trap mockTrap;

    @BeforeEach
    void setUp() {
        context = new SearchContextFixture();
        verifier = new SearchStrategyTestVerifier(context.mockScene, context.mockDungeon);

        mockHero = MockHero.create();
        mockTrapShape = mock(Shape.class);
        mockDoorShape = mock(Shape.class);
        mockTrap = mock(Trap.class);

        strategy = new UnintentionalSearchStrategy(context.mockDungeon, context.fakeLogger, context.mockScene, context.mockAudio, mockTrapShape, mockDoorShape);

        mockTrap.canBeSearched = true;

        when(mockTrapShape.getCells(any(Integer.class))).thenReturn(List.of(1));
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of(2));
        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(true);
        when(context.mockDungeon.getTrap(any(Integer.class))).thenReturn(mockTrap);
    }

    @Test
    void does_not_search_on_hero_position() {
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of());
        mockHero.pos = 1;

        strategy.execute(mockHero);

        verifyNever(context.mockDungeon).getCell(any(Integer.class));
    }

    @Test
    void does_not_search_when_cell_is_out_of_field_of_view() {
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of());
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
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of());
        when(context.mockDungeon.getCell(any(Integer.class))).thenReturn(Terrain.SECRET_TRAP);
        when(context.mockDungeon.getDepth()).thenReturn(0);

        try (MockedStatic<Random> random = mockStatic(Random.class)) {
            random.when(Random::Float).thenReturn(0.1f);
            strategy.execute(mockHero);
        }

        verifier.verifyTrapDiscovered();
        verifyOnce(mockHero).chargeTalismanIfPresent(2);
        assertEquals(1, strategy.trapsFound);
    }

    @Test
    void does_not_discover_a_trap_when_roll_is_bad() {
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of());
        when(context.mockDungeon.getCell(any(Integer.class))).thenReturn(Terrain.SECRET_TRAP);
        when(context.mockDungeon.getDepth()).thenReturn(0);

        try (MockedStatic<Random> random = mockStatic(Random.class)) {
            random.when(Random::Float).thenReturn(0.9f);
            strategy.execute(mockHero);
        }

        verifyNever(mockHero).chargeTalismanIfPresent(2);
        assertEquals(0, strategy.trapsFound);
    }

    @Test
    void has_higher_chance_to_detect_a_trap_with_trap_sense_talent() {
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of());
        when(context.mockDungeon.getCell(any(Integer.class))).thenReturn(Terrain.SECRET_TRAP);
        when(context.mockDungeon.getDepth()).thenReturn(0);
        when(mockHero.hasTalent(Talent.TRAP_SENSE)).thenReturn(true);

        try (MockedStatic<Random> random = mockStatic(Random.class)) {
            random.when(Random::Float).thenReturn(0.7f);
            strategy.execute(mockHero);
        }

        verifier.verifyTrapDiscovered();
        verifyOnce(mockHero).chargeTalismanIfPresent(2);
        assertEquals(1, strategy.trapsFound);
    }

    @Test
    void does_check_separate_shapes_for_trap_and_door() {
        when(mockTrapShape.getCells(any(Integer.class))).thenReturn(List.of(1, 2));
        when(mockDoorShape.getCells(any(Integer.class))).thenReturn(List.of(3, 4));
        when(context.mockDungeon.getCell(1)).thenReturn(Terrain.SECRET_TRAP);
        when(context.mockDungeon.getCell(2)).thenReturn(Terrain.SECRET_DOOR);
        when(context.mockDungeon.getCell(3)).thenReturn(Terrain.SECRET_TRAP);
        when(context.mockDungeon.getCell(4)).thenReturn(Terrain.SECRET_DOOR);

        try (MockedStatic<Random> random = mockStatic(Random.class)) {
            random.when(Random::Float).thenReturn(0f);
            strategy.execute(mockHero);
        }

        assertEquals(1, strategy.trapsFound);
        assertEquals(1, strategy.doorFound);
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