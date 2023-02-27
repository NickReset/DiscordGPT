package me.nickrest.discord.manager;

import lombok.Getter;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.commands.*;
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
        register(new CodeGPTCommand()
                .arguments(
                        CommandArgument.of(OptionType.STRING, "text", "The text to generate", true),
                        CommandArgument.of(OptionType.STRING, "language", "The language to generate code in", true)
                ));
        register(new ImageGPTCommand()
                .arguments(
                        CommandArgument.of(OptionType.STRING, "text", "The text to generate", true)
                ));
        register(new PingCommand());
        register(new ServerCountCommand());
    }

    public void register(Command command) {
        commands.add(command);
    }

    public Command getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
