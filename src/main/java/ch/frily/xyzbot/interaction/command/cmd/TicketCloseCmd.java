package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import javassist.NotFoundException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

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
        event.deferReply(true).queue();
        try {
            Ticket ticket = TicketController.getTicketById(event.getChannelIdLong());
            ticket.requestClose(event);

        } catch (SQLException | NotFoundException exception) {
            event.getHook().editOriginal(exception.getMessage()).queue();
        }
    }
}
