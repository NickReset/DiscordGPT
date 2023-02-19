package me.nickrest.discord;

import lombok.Getter;
import lombok.Setter;
import me.nickrest.discord.command.Command;
import me.nickrest.discord.listener.DiscordListener;
import me.nickrest.discord.manager.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // register application commands
        List<SlashCommandData> commandData = new ArrayList<>();
        for(Command command : commandManager.getCommands()) {
            SlashCommandData data = Commands.slash(command.getName(), command.getDescription())
                    .setDefaultPermissions(command.getDefaultMemberPermissions())
                    .setGuildOnly(command.isGuildOnly());

            Arrays.stream(command.getArguments()).forEach((argument -> data.addOption(argument.type(), argument.name(), argument.description(), argument.required())));
            commandData.add(data);
        }

        jda.updateCommands().addCommands(commandData).queue();
    }

}
