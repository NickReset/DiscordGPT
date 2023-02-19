package me.nickrest.discord.command.commands;

import me.nickrest.Main;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import me.nickrest.gpt.ChatGPT;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@CommandInfo(name = "gpt", description = "Generate text using GPT-3")
public class GPTCommand extends Command {

    Map<User, List<String>> memory = new HashMap<>();

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        OptionMapping textOption = event.getOption("text");
        User user = event.getUser();

        if(textOption == null) {
            throw new RuntimeException("text option is null!");
        }

        String text = Objects.requireNonNull(event.getOption("text")).getAsString();
        List<String> userMemory = memory.get(user);

        StringBuilder startQustion = new StringBuilder();
        if (userMemory != null) {
            startQustion.append("Surround code with markdown ticks (```) and put the language after the first set of ticks. Example: ```java\n")
                    .append("Memory:\n")
                    .append(String.join("\n", userMemory))
                    .append("Don't use the memory as a reference, it is just to help you remember what you have said.\n")
                    .append("\n\n");
        } else {
            memory.put(user, userMemory = new ArrayList<>());
        }
        String response = Main.getChatGPT().sendRequest(startQustion + "Question: " + text);

        memory.get(user).add(
                "   Q: " + text + "\n" +
                "   A: " + response + "\n"
        );

        if (userMemory.size() > 5) userMemory.remove(0);

//        String response = gpt.sendRequest("Write a response to this question \"" + text + "\" if the question is a codding question surround it with ``` on both sides. and for the first the first ticks put the language you are using. but only if it is a coding answer.");

        event.reply(Main.getChatGPT().sendRequest(text).trim()).queue();
        //        event.reply(response.trim()).queue();
    }
}
