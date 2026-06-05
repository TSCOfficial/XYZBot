package ch.frily.xyzbot.embed;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Embed used after the Ticket was closed. Displays Ticket Informations
 */
public class TicketClosedOptionsEmbed implements IEmbed {

    @Setter
    private Member member;

    @Setter
    private TextChannel channel;


    @Override
    public String getAuthorName() {
        return member.getEffectiveName();
    }

    @Override
    public String getAuthorIconUrl() {
        return member.getEffectiveAvatarUrl();
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
        return Ticket.getTicketNameWithoutStatus(channel);
    }
}
