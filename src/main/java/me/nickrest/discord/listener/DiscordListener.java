package me.nickrest.discord.listener;

import lombok.AllArgsConstructor;
import me.nickrest.Main;
import me.nickrest.discord.Discord;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.manager.CommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@AllArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final Discord discord;
    private final String logChannelID;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandManager commandManager = discord.getCommandManager();
        Command foundCommand = commandManager.getCommand(event.getName());

        if (foundCommand == null) {
            Main.getLogger().warn("Command not found: " + event.getName());
            return;
        }

        Main.getLogger().info("Handling command: " + event.getName());
        foundCommand.handle(event);
//        Objects.requireNonNull(event.getJDA().getTextChannelById(logChannelID)).sendMessage("Command executed: " + event.getCommandString() + "\nExecutor: " + event.getUser().getName() + "(" + event.getUser().getId() + ")").queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
//        Objects.requireNonNull(event.getJDA().getTextChannelById(logChannelID)).sendMessage("Bot is ready!").queue();
    }
}
