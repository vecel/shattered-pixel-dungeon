package com.shatteredpixel.shatteredpixeldungeon;

import com.karandys.todo.Todo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

public class GameContext {
    private final DungeonInterface dungeon;
    private final GameLogger logger;

    public GameContext(DungeonInterface dungeon, GameLogger logger) {
        this.dungeon = dungeon;
        this.logger = logger;
    }

    public DungeonInterface getDungeon() {
        return dungeon;
    }

    public GameLogger getLogger() {
        return logger;
    }

    @Todo("Think how to implement without static call")
    public Hero getHero() {
        return Dungeon.hero;
    }
}
