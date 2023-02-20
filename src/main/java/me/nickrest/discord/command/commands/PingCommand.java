package me.nickrest.discord.command.commands;

import me.nickrest.discord.command.Command;
import me.nickrest.discord.command.data.CommandInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "ping", description = "Pong!")
public class PingCommand extends Command {

    @Override
    public void handle(@NotNull SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Fetching Bots Ping!")
                .setEphemeral(true)
                .flatMap(v -> event.getHook().editOriginalFormat("Ping: %d ms", System.currentTimeMillis() - time))
                .queue();
    }

}
