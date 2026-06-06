package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.util.TicketStatus;
import ch.frily.xyzbot.embed.TicketCloseAcceptedEmbed;
import ch.frily.xyzbot.embed.TicketClosedOptionsEmbed;
import javassist.NotFoundException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
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
        event.deferReply(true).queue();
        try {
            // Edit requestMessage
            TicketCloseRequestAcceptBtn acceptBtn = new TicketCloseRequestAcceptBtn();
            TicketCloseRequestRejectBtn rejectBtn = new TicketCloseRequestRejectBtn();
            acceptBtn.setDisabled(true);
            rejectBtn.setDisabled(true);

            TicketCloseAcceptedEmbed acceptedEmbed = new TicketCloseAcceptedEmbed();
            acceptedEmbed.setMember(event.getMember());
            acceptedEmbed.setChannel(event.getChannel().asTextChannel());

            event.getMessage().editMessageEmbeds(acceptedEmbed.build())
                    .setComponents(
                            ActionRow.of(
                                    acceptBtn.build(),
                                    rejectBtn.build()
                            )
                    )
                    .queue();

            // Send options
            Ticket ticket = TicketController.getTicketById(event.getChannelIdLong());
            ticket.close();
            ActionRow actionRow = ActionRow.of(List.of(
                    new TicketDeleteBtn().build(),
                    new TicketArchiveBtn().build()
            ));

            TicketClosedOptionsEmbed optionEmbed = new TicketClosedOptionsEmbed();
            optionEmbed.setMember(event.getMember());
            optionEmbed.setChannel(event.getChannel().asTextChannel());
            event.getHook().sendMessageEmbeds(optionEmbed.build()).addComponents(actionRow).queue();
        } catch (SQLException | NotFoundException sqlException) {
            log.error(sqlException.getMessage());
        }
    }
}
