package me.nickrest.discord.command.data;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public record CommandArgument(OptionType type, String name, String description, boolean required) {

    public static CommandArgument of(OptionType type, String name, String description, boolean required) {
        return new CommandArgument(type, name, description, required);
    }
}
