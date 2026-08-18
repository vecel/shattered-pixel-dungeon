package com.shatteredpixel.shatteredpixeldungeon.mechanics.searching;

import com.shatteredpixel.shatteredpixeldungeon.DungeonInterface;
import com.shatteredpixel.shatteredpixeldungeon.audio.Audio;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameSceneInterface;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

public class SearchContext {

    private final DungeonInterface dungeon;
    private final GameSceneInterface scene;
    private final GameLogger logger;
    private final Audio audio;

    public SearchContext(DungeonInterface dungeon, GameSceneInterface scene, GameLogger logger, Audio audio) {
        this.dungeon = dungeon;
        this.scene = scene;
        this.logger = logger;
        this.audio = audio;
    }

    public DungeonInterface getDungeon() {
        return dungeon;
    }

    public GameLogger getLogger() {
        return logger;
    }

    public GameSceneInterface getScene() {
        return scene;
    }

    public Audio getAudio() {
        return audio;
    }
}
