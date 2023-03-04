package me.nickrest.discord.command.commands;

import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

@CommandInfo(name = "listguilds", description = "Lists all guilds the bot is in.", devOnly = true)
public class ListGuildsCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int page = event.getOption("page") == null ? 1 : Objects.requireNonNull(event.getOption("page")).getAsInt();

        EmbedBuilder builder = new EmbedBuilder().setTitle("Server Count `" + event.getJDA().getGuilds().size() + "`" + " | Page `" + page + "`");

        int count = 0;
        for (Guild guild : event.getJDA().getGuilds()) {
            count++;

            String guildInfo =
                    "Guild: " + guild.getName() + " `" + guild.getId() + "`\n" +
                    "Owner: " + (guild.getOwner() == null ? "Unknown" : "`" + guild.getOwner().getUser().getAsTag() + "`") + "\n" +
                    "Members: " + guild.getMemberCount() + "\n";

            builder.addField("Guild Info", guildInfo, false);

            if(count == page) {
                event.replyEmbeds(
                        builder
                                .setColor(event.getMember() == null ? new Color(0) : event.getMember().getColor())
                                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                                .build()
                        )
                        .setEphemeral(true)
                        .queue();
                return;
            }
        }
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
