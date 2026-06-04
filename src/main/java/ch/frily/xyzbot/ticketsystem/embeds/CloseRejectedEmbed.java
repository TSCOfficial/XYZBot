package ch.frily.xyzbot.ticketsystem.embeds;

import ch.frily.xyzbot.ticketsystem.TicketController;
import ch.frily.xyzbot.utils.IEmbed;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

public class CloseRejectedEmbed implements IEmbed {

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
        return "❌ Schliessanfrage abgelehnt";
    }

    @Override
    public String getDescription() {
        return member.getAsMention() + " hat eine Schliessanfrage abgelehnt.";
    }

    @Override
    public Color getColor() {
        return member.getColors().getPrimary();
    }

    @Override
    public String getFooterText() {
        return TicketController.getTicketNameWithoutStatus(channel);
    }
}
