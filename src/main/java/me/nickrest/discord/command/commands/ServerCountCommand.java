package me.nickrest.discord.command.commands;

import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "servercount", description = "Shows the server count of the bot.", guildOnly = false)
public class ServerCountCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Server Count")
                .setDescription("I am currently in " + event.getJDA().getGuilds().size() + " servers!")
                .build();

        event.getHook().sendMessageEmbeds(embed).queue();
    }

}
