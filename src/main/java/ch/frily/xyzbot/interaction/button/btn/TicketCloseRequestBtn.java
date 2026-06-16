package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.embed.TicketCloseRequestEmbed;
import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketRepository;
import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.util.MessageUtil;
import javassist.NotFoundException;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TicketCloseRequestBtn implements IButton {
    @Override
    public String getId() {
        return "ticket-close-request-btn";
    }

    @Override
    public String getLabel() {
        return "Schliessen";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.DANGER;
    }

    @Override
    public EmojiUnion getEmoji() {
        return Emoji.fromFormatted("🔒");
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        event.deferReply().queue();
        try {
            Ticket ticket = TicketRepository.getTicketById(event.getChannelIdLong());
            ticket.requestClose(event.getUser());

            ActionRow actionRow = ActionRow.of(
                    new TicketCloseRequestAcceptBtn().build(),
                    new TicketCloseRequestRejectBtn().build()
            );

            TicketCloseRequestEmbed optionsEmbed = new TicketCloseRequestEmbed();
            optionsEmbed.setInitiator(event.getMember());
            optionsEmbed.setTicket(ticket);

            event.getHook().sendMessage(ticket.getOwner().getAsMention()).addEmbeds(optionsEmbed.build()).setComponents(actionRow).queue();

            event.getMessage().editMessageComponents(MessageUtil.disableAllMessageComponents(event.getMessage())).queue();
        } catch (SQLException | NotFoundException | IllegalStateException | PermissionException exception) {
            event.getHook().editOriginal(exception.getMessage()).queue();
        }
    }
}
