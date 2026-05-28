package ch.frily.xyzbot.listeners;

import ch.frily.xyzbot.slashcommands.ISlashCommand;
import ch.frily.xyzbot.slashcommands.SlashCommandManager;
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
        log.debug("Slashinteraction");
        for (ISlashCommand command : SlashCommandManager.getInstance().getSlashCommands()){
            if (command.getName().equals(event.getName())){
                command.execute(event);
                return;
            }
        }
    }

    /**
     * Execute the autocompletion for a slash command
     * @param event When an autocompletion is triggered by discord
     */
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        for (ISlashCommand command : SlashCommandManager.getInstance().getSlashCommands()) {
            if (command.getName().equals(event.getName())) {
                String focusedOptionName = event.getFocusedOption().getName();
                List<?> choices = command.getAutocomplete().getOrDefault(focusedOptionName, List.of());

                List<Command.Choice> options = choices.stream()
                        .filter(
                                choice -> choice.toString().startsWith(event.getFocusedOption().getValue()))
                        .map(choice -> {
                            if (choice instanceof String) {
                                return new Command.Choice((String) choice, (String) choice);
                            } else if (choice instanceof Integer) {
                                return new Command.Choice(choice.toString(), (Integer) choice);
                            } else if (choice instanceof Double) {
                                return new Command.Choice(choice.toString(), (Double) choice);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                event.replyChoices(options).queue();
            }
        }
    }
}
