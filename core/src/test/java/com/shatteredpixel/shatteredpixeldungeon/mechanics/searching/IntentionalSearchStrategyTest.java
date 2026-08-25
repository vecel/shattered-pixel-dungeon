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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.utils.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.GameLoggerFake;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogEntry;
import com.shatteredpixel.shatteredpixeldungeon.utils.logger.LogLevel;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class IntentionalSearchStrategyTest {

    private IntentionalSearchStrategy strategy;

    private DungeonInterface mockDungeon;
    private GameSceneInterface mockScene;
    private GameLoggerFake fakeLogger;
    private Audio mockAudio;
    private Shape mockShape;
    private Hero mockHero;
    private Trap mockTrap;

    private MockedConstruction<CheckedCell> mockedEffect;

    @BeforeEach
    void setUp() {
        SearchContextFixture context = new SearchContextFixture();

        mockDungeon = context.mockDungeon;
        mockScene = context.mockScene;
        mockAudio = context.mockAudio;
        mockShape = mock(Shape.class);
        fakeLogger = context.fakeLogger;

        mockHero = MockHero.create();
        mockTrap = mock(Trap.class);
        mockedEffect = mockConstruction(CheckedCell.class);

        strategy = new IntentionalSearchStrategy(mockDungeon, fakeLogger, mockScene, mockAudio, mockShape);

        when(mockShape.getCells(any(Integer.class))).thenReturn(List.of(1));
        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(true);
        when(mockDungeon.getTrap(any(Integer.class))).thenReturn(mockTrap);
    }

    @AfterEach
    void tearDown() {
        mockedEffect.close();
    }

    @Test
    void does_not_search_on_hero_position() {
        when(mockShape.getCells(any(Integer.class))).thenReturn(List.of(1, 2));
        mockHero.pos = 1;

        strategy.execute(mockHero);

        verify(mockScene, times(1)).effectOverFog(any());
    }

    @Test
    void does_not_search_when_cell_is_out_of_field_of_view() {
        when(mockShape.getCells(any(Integer.class))).thenReturn(List.of(1, 2));
        when(mockHero.withinFieldOfView(1)).thenReturn(false);

        strategy.execute(mockHero);

        verify(mockScene, times(1)).effectOverFog(any());
    }

    @Test
    void stops_searching_if_there_is_no_secret() {
        when(mockDungeon.hasSecretAt(any(Integer.class))).thenReturn(false);

        strategy.execute(mockHero);

        verify(mockDungeon, never()).getCell(any(Integer.class));
    }

    @Test
    void discovers_a_trap() {
        when(mockDungeon.getCell(any(Integer.class))).thenReturn(Terrain.SECRET_TRAP);
        mockTrap.canBeSearched = true;

        strategy.execute(mockHero);

        verify(mockHero, times(1)).chargeTalismanIfPresent(2);
        verify(mockScene, times(1)).discover(1, Terrain.SECRET_TRAP);
        verify(mockScene, times(1)).discoverWithScrollOfMagicMapping(1);
        verify(mockDungeon, times(1)).discoverCell(1);
        assertEquals(1, strategy.trapsFound);
        assertEquals(0, strategy.doorFound);
    }

    @Test
    void does_not_discover_unsearchable_trap() {
        when(mockDungeon.getCell(any(Integer.class))).thenReturn(Terrain.SECRET_TRAP);
        mockTrap.canBeSearched = false;

        strategy.execute(mockHero);

        verifyNever(mockHero).chargeTalismanIfPresent(2);
        verifyNever(mockScene).discover(1, Terrain.SECRET_TRAP);
        verifyNever(mockScene).discoverWithScrollOfMagicMapping(1);
        verifyNever(mockDungeon).discoverCell(1);
        assertEquals(0, strategy.trapsFound);
    }

    @Test
    void discovers_door() {
        when(mockDungeon.getCell(any(Integer.class))).thenReturn(Terrain.SECRET_DOOR);

        strategy.execute(mockHero);

        verifyOnce(mockHero).chargeTalismanIfPresent(10);
        verifyOnce(mockScene).discover(1, Terrain.SECRET_DOOR);
        verifyOnce(mockScene).discoverWithScrollOfMagicMapping(1);
        verifyOnce(mockDungeon).discoverCell(1);
        assertEquals(0, strategy.trapsFound);
        assertEquals(1, strategy.doorFound);
    }

    @Test
    void spends_time_and_affects_hunger() {
        Hunger mockHunger = mockHero.getBuff(Hunger.class);
        int hungerValue = -4;
        mockHero.pos = 0;

        strategy.executePostSearchAction(mockHero);

        verifyOnce(mockHero.sprite).showStatus(CharSprite.DEFAULT, Messages.get(Hero.class, "search"));
        verifyOnce(mockHero.sprite).operate(0);
        verifyOnce(mockHero).spendAndNext(Hero.TIME_TO_SEARCH);
        verifyOnce(mockHunger).affectHunger(hungerValue);
    }

    @Test
    void logs_and_affects_hunger_badly_when_hero_has_cursed_talisman() {
        TalismanOfForesight.Foresight talisman = mock(TalismanOfForesight.Foresight.class);

        when(mockHero.hasBuff(TalismanOfForesight.Foresight.class)).thenReturn(true);
        when(mockHero.getBuff(TalismanOfForesight.Foresight.class)).thenReturn(talisman);
        when(talisman.isCursed()).thenReturn(true);

        Hunger mockHunger = mockHero.getBuff(Hunger.class);
        int hungerValue = -10;

        LogEntry log = new LogEntry(LogLevel.NEGATIVE, Messages.get(Hero.class, "search_distracted"));

        strategy.executePostSearchAction(mockHero);

        verifyOnce(mockHunger).affectHunger(hungerValue);
        assertTrue(fakeLogger.contains(log));
    }

    @Test
    void logs_and_plays_audio_when_hero_did_found_anything() {
        strategy.trapsFound = 1;

        strategy.executePostSearchAction(mockHero);

        LogEntry log = new LogEntry(LogLevel.WARNING, Messages.get(Hero.class, "noticed_smth"));

        assertTrue(fakeLogger.contains(log));
        verifyOnce(mockAudio).play(Assets.Sounds.SECRET);
        verifyOnce(mockHero).interrupt();
    }
}