package me.nickrest.discord.command.commands;

import me.nickrest.Main;
import me.nickrest.discord.Discord;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@CommandInfo(name = "listguilds", description = "Lists all guilds the bot is in.", devOnly = true)
public class ListGuildsCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        int page = event.getOption("page") == null ? 1 : Objects.requireNonNull(event.getOption("page")).getAsInt();

        event.replyEmbeds(getEmbedForPage(page, event.getMember()))
                .setActionRow(
                        Button.secondary("listguilds:page:" + (Math.min(page - 1, 0)), "Previous"),
                        Button.secondary("listguilds:page:" + (Math.max((page + 1), event.getJDA().getGuilds().size())), "Next")
                )
                .setEphemeral(true)
                .queue();
    }

    public MessageEmbed getEmbedForPage(int page, Member requestedBy) {
        Discord discord = Main.getDiscord();
        JDA jda = discord.getJda();

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Server Count `" + jda.getGuilds().size() + "`" + " | Page `" + page + "`");

        if(requestedBy == null) return builder.build();

        int count = 0, count2 = 1;
        for (Guild g : jda.getGuilds()) {
            count++;

            if(count != page) {
                count2++;

                if(count2 == 5) {
                    count++;
                    count2 = 0;
                }
            }

            if(count != page) continue;

            builder.addField("Guild Info",
                    "Guild: " + g.getName() + " `" + g.getId() + "`\n" +
                    "Owner: " + (g.getOwner() == null ? "Unknown" : "`" + Objects.requireNonNull(g.getOwner()).getUser().getAsTag() + "`") + "\n" +
                    "Members: " + g.getMemberCount() + "\n", false)
                    .setThumbnail(g.getIconUrl() == null ? jda.getSelfUser().getAvatarUrl() : g.getIconUrl());
        }
        return builder
                .setColor(requestedBy.getColor())
                .setFooter("Requested by " + requestedBy.getUser().getAsTag(), requestedBy.getUser().getAvatarUrl())
                .build();
    }

}
