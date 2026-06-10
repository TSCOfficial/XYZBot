package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketRepository;
import ch.frily.xyzbot.interaction.button.IButton;
import javassist.NotFoundException;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TicketDeleteBtn implements IButton {
    @Override
    public String getId() {
        return "ticket-delete-btn";
    }

    @Override
    public String getLabel() {
        return "Löschen";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.PRIMARY;
    }

    @Override
    public EmojiUnion getEmoji() {
        return Emoji.fromFormatted("🗑️");
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        event.deferReply().queue();
        try {
            Ticket ticket = TicketRepository.getTicketById(event.getChannelIdLong());
            ticket.generateTranscript().thenAccept(fileUpload -> {
                // send in transkript channel
                return;
            });

        } catch (SQLException | NotFoundException e) {
            event.getHook().sendMessage(e.getMessage()).queue(); // todo möglichkeit geben den Channel so zu löschen, Sofern in Kategorie ticket??
        }
    }
}
