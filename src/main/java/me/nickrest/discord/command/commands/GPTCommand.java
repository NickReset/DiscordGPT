package me.nickrest.discord.command.commands;

import lombok.Getter;
import me.nickrest.Main;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import java.util.*;

@CommandInfo(name = "gpt", description = "Generate text using GPT-3")
public class GPTCommand extends Command {

    @Getter
    private final Map<String, List<String>> memory = new HashMap<>();

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        String startingText = "Hello! You are a bot named GPT that can talk to humans from Discord. Surround code with markdown ticks (```) and put the language after the first set of ticks. Only if the response has a code block." +
//                "\nUser: " +
//                "   ID: " + event.getUser().getId() +
//                "   Username: " + event.getUser().getName() +
//                "   Discriminator: " + event.getUser().getDiscriminator() +
//                "   Avatar: " + event.getUser().getAvatarUrl() +
//                "   How to ping: <@" + event.getUser().getId() + ">" +
//                "   You are created by NickRest#3939 & God Mode#0949" +
//                "   Make all responses sound human";
                "\nYou are talking to the user " + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + " (" + event.getUser().getId() + ")"
                + "The url to his/her avatar is " + event.getUser().getAvatarUrl()
                + "You can ping him/her by typing <@" + event.getUser().getId() + ">"
                + "You are created by OpenAI but you were modified by NickRest#3939 & God Mode#0949 to have a memory and to be able to talk to humans from Discord."
                + "Make all responses sound human.";
        OptionMapping textOption = event.getOption("text");
        event.deferReply().queue();
        User user = event.getUser();

        if(textOption == null) {
            throw new RuntimeException("text option is null!");
        }

        String text = Objects.requireNonNull(event.getOption("text")).getAsString();
        String response;
        List<String> userMemory = memory.computeIfAbsent(user.getId(), k -> new ArrayList<>());

        if (userMemory.size() == 0) {
            response = Main.getChatGPT().sendRequest(startingText + "\n" + text);
        } else {
            StringBuilder memoryBuilder = new StringBuilder();
            for (String s : userMemory) {
                memoryBuilder.append(s);
            }
            memoryBuilder.append("Q: ").append(text).append("\n").append("A: ");
            response = Main.getChatGPT().sendRequest(startingText + "\n" + memoryBuilder);
        }
        try {
            Main.getLogger().info("Attempting to send response to the user.");
            event.getHook().editOriginal(response).queue();
            Main.getLogger().info("Successfully sent response to the user.");
        } catch (Exception e) {
            Main.getLogger().warn("Failed to send response to the user, sending to hastebin instead.");
            event.getHook().editOriginal(Main.getHastebin().sendRequest(response, user.getAsTag())).queue();
        }
        userMemory.add("Q: " + text + "\nA: " + response + "\n");
        if (userMemory.size() > 10) {
            userMemory.remove(0);
        }
    }
}
