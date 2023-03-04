package me.nickrest.discord.listener;

import lombok.AllArgsConstructor;
import me.nickrest.Main;
import me.nickrest.discord.Discord;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.manager.CommandManager;
import me.nickrest.util.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

<<<<<<< HEAD
import java.util.List;
=======
import java.util.Objects;
>>>>>>> d2de53903b047586cb1cdfb507358f76428ce07a

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
                        .setDescription("You must be a developer of" + Main.getDiscord().getJda().getSelfUser().getName() +  " to use this command.")
                        .setColor(0xff0000)
                        .build();

                event.replyEmbeds(embed).queue();
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
//        Objects.requireNonNull(event.getJDA().getTextChannelById(logChannelID)).sendMessage("Command executed: " + event.getCommandString() + "\nExecutor: " + event.getUser().getName() + "(" + event.getUser().getId() + ")").queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
//        Objects.requireNonNull(event.getJDA().getTextChannelById(logChannelID)).sendMessage("Bot is ready!").queue();
    }
}
