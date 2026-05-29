package ch.frily.xyzbot.listeners;

import ch.frily.xyzbot.slashcommands.ISlashCommand;
import ch.frily.xyzbot.slashcommands.SlashCommandRegistry;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class InteractionListener extends ListenerAdapter {

    private static InteractionListener instance;

    public static InteractionListener getInstance() {
        if (instance == null) {
            instance = new InteractionListener();
        }
        return instance;
    }

    /**
     * Gets triggered as soon as a slash command is executed
     * @param event SlashCommandInteractionEvent
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        try {
            SlashCommandRegistry.getInstance().dispatchInteractionEvent(event);
        } catch (NotFoundException notFoundException) {
            event.reply(notFoundException.getMessage()).setEphemeral(true).queue();
        }
    }

    /**
     * Execute the autocompletion for a slash command
     * @param event When an autocompletion is triggered by discord
     */
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        SlashCommandRegistry.getInstance().dispatchAutocompleteEvent(event);
    }
}
