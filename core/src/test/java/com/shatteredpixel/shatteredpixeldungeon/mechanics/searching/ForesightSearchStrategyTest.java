package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.utils.GdxApplicationExtension;
import com.shatteredpixel.shatteredpixeldungeon.utils.MockHero;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Shape;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(GdxApplicationExtension.class)
class ForesightSearchStrategyTest {
    private ForesightSearchStrategy strategy;
    private SearchContextFixture context;
    private SearchStrategyTestVerifier verifier;
    private Shape mockShape;
    private Hero mockHero;
    private Trap mockTrap;

    @BeforeEach
    void setUp() {
        context = new SearchContextFixture();
        verifier = new SearchStrategyTestVerifier(context.mockScene, context.mockDungeon);

        mockHero = MockHero.create();
        mockShape = mock(Shape.class);;
        mockTrap = mock(Trap.class);

        strategy = new ForesightSearchStrategy(context.mockDungeon, context.mockScene, mockShape);

        mockTrap.canBeSearched = true;

        when(mockShape.getCells(any(Integer.class))).thenReturn(List.of(1));
        when(mockHero.withinFieldOfView(any(Integer.class))).thenReturn(true);
        when(context.mockDungeon.getTrap(any(Integer.class))).thenReturn(mockTrap);
    }

    @Test
    void updates_fog_in_post_search_Action() {
        mockHero.pos = 0;
        strategy.executePostSearchAction(mockHero);

        verifyOnce(context.mockScene).updateFog(0, Foresight.DISTANCE + 1);
    }
}