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
        try {
            Main.getLogger().info("Attempting to send memory to the user.");
            event.getHook().editOriginal(text).queue();
        } catch (Exception e) {
            Main.getLogger().warn("Failed to send memory to the user, sending to hastebin instead.");
            event.getHook().editOriginal(Main.getHastebin().sendRequest(text, event.getUser().getAsTag())).queue();
        }
    }
}
