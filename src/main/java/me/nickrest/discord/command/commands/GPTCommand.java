package me.nickrest.discord.command.commands;

import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "gpt", description = "Generate text using GPT-3")
public class GPTCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {

    }
}
