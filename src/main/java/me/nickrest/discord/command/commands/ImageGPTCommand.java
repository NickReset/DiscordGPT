package me.nickrest.discord.command.commands;

import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@CommandInfo(name = "imagegpt", description = "Generates an image with GPT-3")
public class ImageGPTCommand extends Command {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String text = Objects.requireNonNull(event.getOption("text")).getAsString();
        try {
            event.getHook().editOriginal("https://image.pollinations.ai/prompt/" + URLEncoder.encode(text, StandardCharsets.UTF_8)).queue();
        } catch (Exception e) {
            event.getHook().editOriginal("Error: " + e.getMessage()).queue();
        }
    }
}
