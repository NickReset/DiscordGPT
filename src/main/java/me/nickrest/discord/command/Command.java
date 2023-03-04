package me.nickrest.discord.command;

import lombok.Getter;
import lombok.Setter;
import me.nickrest.discord.command.data.CommandArgument;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter @Setter
public abstract class Command {

    private final CommandInfo info = getClass().getAnnotation(CommandInfo.class);

    private String name, description;
    private DefaultMemberPermissions defaultMemberPermissions;
    private CommandArgument[] arguments = {};
    private ButtonHandler[] buttonHandlers = {};

    private boolean guildOnly, devOnly;

    public Command() {
        if(info == null) {
            throw new RuntimeException("CommandInfo annotation not found!");
        }

        name = info.name();
        description = info.description();
        defaultMemberPermissions = DefaultMemberPermissions.ENABLED;
        guildOnly = info.guildOnly();
        devOnly = info.devOnly();
    }

    public abstract void handle(@NotNull SlashCommandInteractionEvent event);

    public Command arguments(CommandArgument... arguments) {
        this.arguments = arguments;
        return this;
    }

    public Command buttonHandlers(ButtonHandler... buttonHandlers) {
        this.buttonHandlers = buttonHandlers;
        return this;
    }

    public ButtonHandler getButtonHandler(String componentId) {
        return Arrays.stream(buttonHandlers).filter(buttonHandler -> componentId.startsWith(buttonHandler.getPrefix())).findFirst().orElse(null);
    }
}
