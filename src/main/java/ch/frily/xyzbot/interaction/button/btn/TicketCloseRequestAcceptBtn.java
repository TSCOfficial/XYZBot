package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketRepository;
import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.embed.TicketCloseAcceptedEmbed;
import ch.frily.xyzbot.embed.TicketClosedOptionsEmbed;
import ch.frily.xyzbot.util.MessageUtil;
import javassist.NotFoundException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class TicketCloseRequestAcceptBtn implements IButton {

    @Setter
    private boolean disabled = false;

    @Override
    public String getId() {
        return "ticket-close-request-accept-btn";
    }

    @Override
    public String getLabel() {
        return "Ja, schliessen";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.PRIMARY;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        event.deferReply().queue();
        try {
            Ticket ticket = TicketRepository.getTicketById(event.getChannelIdLong());
            log.debug("sending close request to ticket");
            ticket.acceptCloseRequest(event.getMember());

            TicketCloseAcceptedEmbed acceptedEmbed = new TicketCloseAcceptedEmbed();
            acceptedEmbed.setMember(event.getMember());
            acceptedEmbed.setTicket(ticket);

            event.getMessage().editMessageEmbeds(acceptedEmbed.build())
                    .setComponents(MessageUtil.disableAllMessageComponents(event.getMessage()))
                    .queue();

            // Send options
            ActionRow actionRow = ActionRow.of(List.of(
                    new TicketDeleteBtn().build()
            ));

            TicketClosedOptionsEmbed optionEmbed = new TicketClosedOptionsEmbed();
            optionEmbed.setTicket(ticket);
            event.getHook().sendMessageEmbeds(optionEmbed.build()).addComponents(actionRow).queue();

        } catch (SQLException | NotFoundException | PermissionException exception) {
            log.error(exception.getMessage());
        }
    }
}
