package me.nickrest.discord.listener;

import lombok.AllArgsConstructor;
import me.nickrest.Main;
import me.nickrest.discord.Discord;
import me.nickrest.discord.command.ButtonHandler;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.manager.CommandManager;
import me.nickrest.util.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

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

        Config config = Main.getConfig();

        if(config.has("devs") && foundCommand.isDevOnly()) {
            List<Object> devIds = config.getList("devs");

            if(!devIds.contains(event.getUser().getId())) {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Error")
                        .setDescription("You must be a developer of `1`" + Main.getDiscord().getJda().getSelfUser().getName() +  "` to use this command.")
                        .setColor(0xff0000)
                        .build();

                event.replyEmbeds(embed).setEphemeral(true).queue();
                return;
            }
        }

        if(foundCommand.isDevOnly() && !config.has("devs")) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Error")
                    .setDescription("Config is missing devs list.")
                    .setColor(0xff0000)
                    .build();

            event.replyEmbeds(embed).queue();
            return;
        }

        Main.getLogger().info("Handling command: " + event.getName());
        foundCommand.handle(event);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        CommandManager commandManager = discord.getCommandManager();
//
        for(Command command : commandManager.getCommands()) {
            ButtonHandler handler = command.getButtonHandler(event.getComponentId());

            if(handler != null) {
                handler.handle(event);
                return;
            }
        }
    }
}
