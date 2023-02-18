package me.nickrest.discord.manager;

import lombok.Getter;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.commands.GPTCommand;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        register(new GPTCommand());
    }

    public void register(Command command) {
        commands.add(command);
    }

    public Command getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
