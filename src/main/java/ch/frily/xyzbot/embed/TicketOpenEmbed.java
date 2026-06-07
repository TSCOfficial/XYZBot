package ch.frily.xyzbot.embed;

import ch.frily.xyzbot.feature.Ticket;
import lombok.Setter;

import java.awt.*;

public class TicketOpenEmbed implements IEmbed {

    @Setter
    private Ticket ticket;

    @Override
    public String getAuthorIconUrl() {
        return ticket.getOwner().getEffectiveAvatarUrl();
    }

    @Override
    public Color getColor() {
        return ticket.getOwner().getColors().getPrimary();
    }

    @Override
    public String getTitle() {
        return ticket.getType().getLabel();
    }

    @Override
    public String getDescription() {
        return "Willkommen **" + ticket.getOwner().getUser().getGlobalName() + "**!" +
                "\n" +
                ticket.getType().getEmbedDescription() +
                "\n" +
                "-# Mit /ticket add können weitere benutzer Hinzugefügt werden.";
    }

    @Override
    public String getFooterText() {
        return ticket.getNameWithoutStatus();
    }
}
