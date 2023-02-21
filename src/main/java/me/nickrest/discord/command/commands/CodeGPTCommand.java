package me.nickrest.discord.command.commands;

import me.nickrest.Main;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@CommandInfo(name = "codegpt", description = "Generates code with GPT-3")
public class CodeGPTCommand extends Command {
    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        String text = Objects.requireNonNull(event.getOption("text")).getAsString();
        String language = Objects.requireNonNull(event.getOption("language")).getAsString();

        String textToSend = String.format("/* Create a programming response in %s to this response: %s */", language, text);

        String response = Main.getChatGPT().sendCodingRequest(textToSend);
        // remove every + at the beginning of each line
        response = response.replaceAll("^(\\+)", "");

        String formattedResponse = String.format("```%s\n%s\n```", language, response);

        try {
            Main.getLogger().info("Attempting to send code response to the user.");
            event.getHook().editOriginal(formattedResponse).queue();
        } catch (Exception e) {
            Main.getLogger().warn("Failed to send code response to the user, sending to hastebin instead.");
            event.getHook().editOriginal(Main.getHastebin().sendRequest(formattedResponse, event.getUser().getAsTag())).queue();
        }

    }
}
