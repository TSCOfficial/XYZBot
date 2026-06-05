package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.embed.TicketCloseRequestEmbed;
import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import ch.frily.xyzbot.interaction.button.IButton;
import javassist.NotFoundException;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
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
        event.deferReply(true).queue();
        try {
            Ticket ticket = TicketController.getTicketById(event.getChannelIdLong());
            ticket.requestClose(event);

        } catch (SQLException | NotFoundException exception) {
            event.getHook().editOriginal(exception.getMessage()).queue();
        }
    }
}
