package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import ch.frily.xyzbot.feature.action.TicketCloseRequestAction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class TicketCloseCmd implements ISlashSubcommand {
    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Erstelle eine Schliessanfrage";
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        new TicketCloseRequestAction().execute(event);
    }
}
