package com.karandys.shatteredpixeldungeon.console.commands;

import com.shatteredpixel.shatteredpixeldungeon.GameContext;

public interface ConsoleCommand {
    String name();
    String description();
    void execute(GameContext context, String[] args);
}
