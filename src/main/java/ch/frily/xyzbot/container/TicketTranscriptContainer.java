package ch.frily.xyzbot.container;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.filedisplay.FileDisplay;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.utils.FileUpload;

@Slf4j
public class TicketTranscriptContainer extends Container {

    public TicketTranscriptContainer(Ticket ticket, FileUpload transkript) {
        this.addComponent(TextDisplay.of(MessageUtil.format("### Ticket {} wurde geschlossen", ticket.getNameWithoutStatus())));
        this.addComponent(Separator.createInvisible(Separator.Spacing.SMALL));
        this.addComponent(TextDisplay.of(MessageUtil.format("**Kategorie**: {} / {}", ticket.getType().getGroup().getLabel(), ticket.getType().getLabel())));
        this.addComponent(TextDisplay.of(MessageUtil.format("**Erstellt von**: {} ({})", ticket.getOwner().getAsMention(), ticket.getOwner().getEffectiveName())));
        this.addComponent(TextDisplay.of(MessageUtil.format("**Verantwortlich**: {} ({})", ticket.getAssignee().getAsMention(), ticket.getAssignee().getEffectiveName())));
        this.addComponent(Separator.createDivider(Separator.Spacing.LARGE));
        this.addComponent(FileDisplay.fromFile(transkript));
    }
}
