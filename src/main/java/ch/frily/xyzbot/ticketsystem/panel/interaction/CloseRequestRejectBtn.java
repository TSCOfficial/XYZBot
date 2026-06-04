package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import ch.frily.xyzbot.ticketsystem.embeds.CloseRejectedEmbed;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class CloseRequestRejectBtn implements IButton {

    @Setter
    private boolean disabled = false;

    @Override
    public String getId() {
        return "ticket-closerequest-reject-button";
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
        log.debug("Disabled status: {}", disabled);
        return disabled;
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {

        CloseRequestAcceptBtn acceptBtn = new CloseRequestAcceptBtn();
        CloseRequestRejectBtn rejectBtn = new CloseRequestRejectBtn();
        acceptBtn.setDisabled(true);
        rejectBtn.setDisabled(true);

        CloseRejectedEmbed embed = new CloseRejectedEmbed();
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
