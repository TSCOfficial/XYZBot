package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketRepository;
import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.embed.TicketCloseRejectedEmbed;
import ch.frily.xyzbot.util.EnvResolver;
import ch.frily.xyzbot.util.MessageUtil;
import javassist.NotFoundException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class TicketCloseRequestRejectBtn implements IButton {

    @Setter
    private boolean disabled = false;

    @Override
    public String getId() {
        return "ticket-close-request-reject-btn";
    }

    @Override
    public String getLabel() {
        return "Nein, abbrechen";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.SECONDARY;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {

        try {
            Ticket ticket = TicketRepository.getTicketById(event.getChannelIdLong());
            ticket.rejectCloseRequest(event.getMember());

            TicketCloseRejectedEmbed embed = new TicketCloseRejectedEmbed();
            embed.setMember(event.getMember());
            embed.setTicket(ticket);

            event.editMessageEmbeds(embed.build())
                    .setComponents(MessageUtil.disableAllMessageComponents(event.getMessage()))
                    .queue();

            CompletableFuture<Message> welcomeMessage = EnvResolver.getMessageById(event.getGuild().getIdLong(), ticket.getChannel().getIdLong(), ticket.getWelcomeMessageId());
            welcomeMessage.thenAccept(message -> {
              message.editMessageComponents(MessageUtil.enableAllMessageComponents(message)).queue();
            });
        } catch (SQLException | NotFoundException sqlException){
            event.getHook().editOriginal(sqlException.getMessage()).queue();
        }

    }
}
