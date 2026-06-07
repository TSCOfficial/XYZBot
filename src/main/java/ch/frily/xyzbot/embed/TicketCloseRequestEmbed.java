package ch.frily.xyzbot.embed;

import ch.frily.xyzbot.feature.Ticket;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public class TicketCloseRequestEmbed implements IEmbed {

    @Setter
    private Member member;

    @Setter
    private Ticket ticket;

    @Override
    public String getAuthorName() {
        return member.getEffectiveName();
    }

    @Override
    public String getAuthorIconUrl() {
        return member.getAvatarUrl();
    }

    @Override
    public String getTitle() {
        return "🔓 Schliessanfrage";
    }

    @Override
    public String getDescription() {
        return member.getAsMention() + " hat eine Schliessanfrage gestellt.\nMöchtest du das Ticket schliessen?";
    }

    @Override
    public Color getColor() {
        return member.getColors().getPrimary();
    }

    @Override
    public String getFooterText() {
        return ticket.getNameWithoutStatus();
    }
}
