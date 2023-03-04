package me.nickrest.discord.command.commands;

import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CommandInfo(name = "listguilds", description = "Lists all guilds the bot is in.", devOnly = true)
public class ListGuildsCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Server Count `" + event.getJDA().getGuilds().size() + "`");

        event.getJDA().getGuilds().forEach(guild -> builder
                        .addField("Guild", guild.getName() + " `" + guild.getId() + "`", false)
                        .addField("Owner", guild.getOwner() == null ? "Unknown" : "`" + guild.getOwner().getUser().getAsTag() + "`", false)
                        .addField("Members", String.valueOf(guild.getMemberCount()), false)
                        .setThumbnail(guild.getIconUrl() == null ? guild.getJDA().getSelfUser().getAvatarUrl() : guild.getIconUrl())
        );

        builder.setFooter("Requested by " + event.getUser().getAsTag(), event.getUser().getAvatarUrl());

        event.replyEmbeds(
                builder
                        .setColor(event.getMember() == null ? new Color(0) : event.getMember().getColor())
                        .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                        .build()
                )
                .setEphemeral(true)
                .queue();
    }
}
