package me.nickrest.discord;

import lombok.Getter;
import lombok.Setter;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.listener.DiscordListener;
import me.nickrest.discord.manager.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Getter @Setter
public class Discord {
    private String token;
    private CommandManager commandManager;
    private DiscordListener discordListener;
    private JDA jda;

    public Discord(String token) {
        this.token = token;
        this.commandManager = new CommandManager();
        this.jda = JDABuilder.createDefault(token).build();
        this.jda.addEventListener(this.discordListener = new DiscordListener(this));

        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Command command : commandManager.getCommands()) {
            jda.updateCommands().addCommands(
                    Commands.slash(command.getName(), command.getDescription())
                            .setDefaultPermissions(command.getDefaultMemberPermissions())
                            .setGuildOnly(command.isGuildOnly())
            ).queue();
        }

    }

}
