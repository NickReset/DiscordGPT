package me.nickrest.discord.manager;

import lombok.Getter;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.commands.ClearMemoryCommand;
import me.nickrest.discord.command.commands.GPTCommand;
import me.nickrest.discord.command.commands.ViewMemoryCommand;
import me.nickrest.discord.command.data.CommandArgument;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        register(new GPTCommand()
                .arguments(
                        CommandArgument.of(OptionType.STRING, "text", "The text to generate", true)
                )
        );
        register(new ClearMemoryCommand());
        register(new ViewMemoryCommand());
    }

    public void register(Command command) {
        commands.add(command);
    }

    public Command getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
