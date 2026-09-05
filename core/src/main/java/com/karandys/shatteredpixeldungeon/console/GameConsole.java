package com.karandys.shatteredpixeldungeon.console;

import com.karandys.shatteredpixeldungeon.console.commands.ConsoleCommand;
import com.shatteredpixel.shatteredpixeldungeon.GameContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameConsole {
    private final GameContext context;
    private final Map<String, ConsoleCommand> commands;

    public GameConsole(GameContext context) {
        this.context = context;
        this.commands = new HashMap<>();
    }

    public void register(ConsoleCommand command) {
        commands.put(command.name(), command);
    }

    public void process(String input) {
        if (input == null || input.trim().isEmpty()) return;

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();

        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        ConsoleCommand command = commands.get(commandName);
        if (command != null) {
            command.execute(context, args);
        } else {
            System.out.println("Unknown command: " + commandName);
        }
    }
}
