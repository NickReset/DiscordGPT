package me.nickrest.discord.manager;

import lombok.Getter;
import me.nickrest.Main;
import me.nickrest.discord.command.ButtonHandler;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.commands.*;
import me.nickrest.discord.command.data.CommandArgument;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        register(new ListGuildsCommand()
                .arguments(
                        CommandArgument.of(OptionType.INTEGER, "page", "The page to view", false)
                )
                .buttonHandlers(
                        new ButtonHandler("listguilds:page") {
                            @Override
                            public void handle(@NotNull ButtonInteractionEvent event) {
                                ListGuildsCommand command = (ListGuildsCommand) getCommand("listguilds");
                                int pageInt = Integer.parseInt(event.getComponentId().split(":")[2]);

                                if(pageInt < 1) pageInt = 1;
                                if(pageInt > command.getPageCount()) pageInt = command.getPageCount();

                                event.editMessageEmbeds(command.getEmbedForPage(pageInt, event.getMember()))
                                        .setActionRow(
                                                Button.secondary("listguilds:page:" + (pageInt - 1), "Previous"),
                                                Button.secondary("listguilds:page:" + (pageInt + 1), "Next")
                                        )
                                        .queue();
                            }
                        }
                )
        );
    }

    public void register(Command command) {
        commands.add(command);
    }

    public Command getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
