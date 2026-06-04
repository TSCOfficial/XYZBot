package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.slashcommand.ISlashSubcommand;
import ch.frily.xyzbot.ticketsystem.actions.TicketCloseRequestAction;
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
