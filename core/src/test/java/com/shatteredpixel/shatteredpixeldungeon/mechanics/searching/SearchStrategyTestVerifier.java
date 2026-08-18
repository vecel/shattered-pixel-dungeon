package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import static com.shatteredpixel.shatteredpixeldungeon.utils.MockitoExtension.verifyOnce;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;

public class SearchStrategyTestVerifier {

    private final GameSceneInterface scene;
    private final DungeonInterface dungeon;

    public SearchStrategyTestVerifier(GameSceneInterface scene, DungeonInterface dungeon) {
        this.scene = scene;
        this.dungeon = dungeon;
    }

    public void verifyTrapDiscovered() {
        verifyOnce(scene).discover(any(Integer.class), eq(Terrain.SECRET_TRAP));
        verifyDiscoverCalled();
    }

    public void verifyDoorDiscovered() {
        verifyOnce(scene).discover(any(Integer.class), eq(Terrain.SECRET_DOOR));
        verifyDiscoverCalled();
    }

    private void verifyDiscoverCalled() {
        verifyOnce(scene).discoverWithScrollOfMagicMapping(any(Integer.class));
        verifyOnce(dungeon).discoverCell(any(Integer.class));
    }
}
