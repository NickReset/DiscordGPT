package me.nickrest.discord.listener;

import lombok.AllArgsConstructor;
import me.nickrest.discord.Discord;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.manager.CommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class DiscordListener extends ListenerAdapter {

    private final Discord discord;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandManager commandManager = discord.getCommandManager();
        Command foundCommand = commandManager.getCommand(event.getName());

        if (foundCommand == null)
            return;

        foundCommand.handle(event);
    }
}
