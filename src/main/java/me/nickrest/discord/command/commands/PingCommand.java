package me.nickrest.discord.command.commands;

import me.nickrest.Main;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "ping", description = "Pong!")
public class PingCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        long restPing = event.getJDA().getRestPing().complete(), gatewayPing = event.getJDA().getGatewayPing(), openAIPing = Main.getChatGPT().ping();
        long time = System.currentTimeMillis();

        event.deferReply().setEphemeral(true).flatMap(v -> (
                event.getHook().editOriginalEmbeds(
                        new EmbedBuilder()
                                .setTitle("Pong!")
                                .addField("Round Trip", (System.currentTimeMillis() - time) + "ms", true)
                                .addField("Rest Ping", restPing + "ms", true)
                                .addField("Gateway Ping", gatewayPing + "ms", true)
                                .addField("OpenAI Ping", openAIPing + "ms", true)
                                .build()
                )
        )).queue();
    }

}
