package me.nickrest.discord.command.commands;

import me.nickrest.Main;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "clearmemory", description = "Clears the bots memory of you. *Swoosh!*")
public class ClearMemoryCommand extends Command {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        GPTCommand gptCommand = (GPTCommand) Main.getDiscord().getCommandManager().getCommand("gpt");
        if (gptCommand.getMemory().get(event.getUser().getId()) == null) {
            event.getHook().editOriginal("You have no memory!").queue();
            return;
        }
        gptCommand.getMemory().remove(event.getUser().getId());
        event.getHook().editOriginal("Cleared memory!").queue();
    }
}
