package ch.frily.xyzbot.embed;

import ch.frily.xyzbot.feature.Ticket;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

/**
 * Embed used after the Ticket was closed. Displays Ticket Informations
 */
public class TicketClosedOptionsEmbed implements IEmbed {

    @Setter
    private Ticket ticket;


    @Override
    public String getAuthorName() {
        if (ticket.getAssignee() != null) {
            return ticket.getAssignee().getEffectiveName();
        }
        return null;
    }

    @Override
    public String getAuthorIconUrl() {
        if (ticket.getAssignee() != null) {
            return ticket.getAssignee().getAvatarUrl();
        }
        return null;
    }

    @Override
    public String getTitle() {
        return "Ticket archivieren?";
    }

    @Override
    public String getDescription() {
        return "Soll das Ticket nun archiviert oder entdültig gelöscht werden?\n-# Ein Transkript wird automatisch generiert und hinterlegt.";
    }

    @Override
    public String getFooterText() {
        return ticket.getNameWithoutStatus();
    }
}
