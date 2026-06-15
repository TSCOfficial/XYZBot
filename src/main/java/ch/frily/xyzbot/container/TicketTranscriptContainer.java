package ch.frily.xyzbot.container;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.filedisplay.FileDisplay;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.FileUpload;

import java.time.OffsetDateTime;

@Slf4j
public class TicketTranscriptContainer extends Container {

    public TicketTranscriptContainer(Member initiator, Ticket ticket, FileUpload transkript) {
        this.addComponent(TextDisplay.of(MessageUtil.format("### Ticket {} wurde geschlossen", ticket.getNameWithoutStatus())));
        this.addComponent(Separator.createInvisible(Separator.Spacing.SMALL));
        this.addComponent(TextDisplay.of(MessageUtil.format("**Kategorie**: `{} / {}`", ticket.getType().getGroup().getLabel(), ticket.getType().getLabel())));
        this.addComponent(TextDisplay.of(MessageUtil.format("**Erstellt von**: {} ({})", ticket.getOwner().getAsMention(), ticket.getOwner().getUser().getName())));

        String assignedMember = "**Verantwortlich**: -";
        if (ticket.getAssignee() != null) {
            assignedMember = MessageUtil.format("**Verantwortlich**: {} (@{})", ticket.getAssignee().getAsMention(), ticket.getAssignee().getUser().getName());
        }

        this.addComponent(TextDisplay.of(assignedMember));
        this.addComponent(TextDisplay.of(MessageUtil.format("**Gelöscht von**: {} (@{})", initiator.getAsMention(), initiator.getUser().getName())));

        long epochTimeCreated = ticket.getChannel().getTimeCreated().toEpochSecond();
        String opendTimeSinceCreation = MessageUtil.calcDuration(ticket.getChannel().getTimeCreated(), OffsetDateTime.now());
        this.addComponent(TextDisplay.of(MessageUtil.format("**Geöffnet am**: <t:{}:F> ({})", epochTimeCreated, opendTimeSinceCreation)));
        this.addComponent(Separator.createDivider(Separator.Spacing.LARGE));
        this.addComponent(FileDisplay.fromFile(transkript));
    }
}
