package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.container.TicketTranscriptContainer;
import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketRepository;
import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

@Slf4j
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
                fileUpload.setName("transkript-" + ticket.getNameWithoutStatus());
                TextChannel logChannel = EnvResolver.getChannelById(TextChannel.class, EnvKey.GUILD_XYZCRAFT, EnvKey.CHANNEL_TICKETTRANSKRIPT);
                List<Container> containers = new TicketTranscriptContainer(ticket, fileUpload).build();
                logChannel.sendMessageComponents(containers).useComponentsV2().addFiles(fileUpload).setAllowedMentions(List.of()).queue();

                try {
                    ticket.delete();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).exceptionally(throwable -> {
                log.error(throwable.getMessage());
                return null;
            });

        } catch (SQLException | NotFoundException e) {
            event.getHook().sendMessage(e.getMessage() + "\n\n-# Versuche es später erneut oder frag ein*e Administrator*in das Ticket zu löschen").queue();
        }
    }
}
