package com.karandys.shatteredpixeldungeon.console.commands;

import com.shatteredpixel.shatteredpixeldungeon.GameContext;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

public class TenguMaskCommand implements ConsoleCommand {
    @Override
    public String name() {
        return "mask";
    }

    @Override
    public String description() {
        return "Provides a tengu mask.";
    }

    @Override
    public void execute(GameContext context, String[] args) {
        Hero hero = context.getHero();

        if (hero == null) return;

        TengusMask mask = new TengusMask();
        mask.collect();
    }
}
