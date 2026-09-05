package com.karandys.shatteredpixeldungeon.console.commands;

import com.shatteredpixel.shatteredpixeldungeon.GameContext;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GameLogger;

import java.util.logging.Logger;

public class LevelUpCommand implements ConsoleCommand {
    @Override
    public String name() {
        return "level_up";
    }

    @Override
    public String description() {
        return "Levels up the hero. Usage: level_up [amount]";
    }

    @Override
    public void execute(GameContext context, String[] args) {
        GameLogger logger = context.getLogger();
        Hero hero = context.getHero();

        if (hero == null) return;

        int levelsToAdd = 1;
        if (args.length > 0) {
            try {
                levelsToAdd = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.warning("Error: Invalid number. Usage: level_up [amount]");
                return;
            }
        }

        while (levelsToAdd-- > 0) hero.levelUp();
    }
}
