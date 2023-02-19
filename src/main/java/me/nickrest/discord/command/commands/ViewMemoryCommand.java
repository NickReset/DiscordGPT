package me.nickrest.discord.command.commands;

import me.nickrest.Main;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "viewmemory", description = "View the bots memory of you.")
public class ViewMemoryCommand extends Command {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        GPTCommand gptCommand = (GPTCommand) Main.getDiscord().getCommandManager().getCommand("gpt");
        StringBuilder sb = new StringBuilder();
        if (gptCommand.getMemory().get(event.getUser().getId()) == null) {
            event.getHook().editOriginal("You have no memory!").queue();
            return;
        }
        for (int i = 0; i < gptCommand.getMemory().get(event.getUser().getId()).size(); i++) {
            sb.append((i + 1) + ": " + gptCommand.getMemory().get(event.getUser().getId()).get(i));
        }
        String text = sb.toString();
        // split text into chunks of 2000 characters
        String[] chunks = text.split("/.{1,2000}/g");
        if (chunks.length == 1) {
            event.getHook().editOriginal(chunks[0]).queue();
        } else {
            for (int i = 0; i < chunks.length; i++) {
                if (i == 0) {
                    event.getHook().editOriginal(chunks[i]).queue();
                } else {
                    event.getChannel().sendMessage(chunks[i]).queue();
                }
            }
        }
    }
}
