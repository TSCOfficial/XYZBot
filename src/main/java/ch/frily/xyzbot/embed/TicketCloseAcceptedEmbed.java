package ch.frily.xyzbot.embed;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

public class TicketCloseAcceptedEmbed implements IEmbed {

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
        return "🔒 Schliessanfrage angenommen";
    }

    @Override
    public String getDescription() {
        return member.getAsMention() + " hat die Schliessanfrage angenommen. Das Ticket wird nun geschlossen.";
    }

    @Override
    public Color getColor() {
        return member.getColors().getPrimary();
    }

    @Override
    public String getFooterText() {
        return Ticket.getTicketNameWithoutStatus(channel);
    }
}
