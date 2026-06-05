package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.embed.TicketCloseRejectedEmbed;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

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

        TicketCloseRequestAcceptBtn acceptBtn = new TicketCloseRequestAcceptBtn();
        TicketCloseRequestRejectBtn rejectBtn = new TicketCloseRequestRejectBtn();
        acceptBtn.setDisabled(true);
        rejectBtn.setDisabled(true);

        TicketCloseRejectedEmbed embed = new TicketCloseRejectedEmbed();
        embed.setMember(event.getMember());
        embed.setChannel(event.getChannel().asTextChannel());

        event.editMessageEmbeds(embed.build())
                .setComponents(
                        ActionRow.of(
                                acceptBtn.build(),
                                rejectBtn.build()
                        )
                )
                .queue();
    }
}
