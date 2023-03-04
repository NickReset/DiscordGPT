package me.nickrest.discord.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public abstract class ButtonHandler {

    /** The prefix of the button id ex: page:1 in this example page would be the prefix */
    @Getter
    private final String prefix;

    /**
     * Handles the button interaction event.
     *
     * @param event the event
     */
    public abstract void handle(@NotNull ButtonInteractionEvent event);
}
