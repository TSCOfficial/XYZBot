package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import ch.frily.xyzbot.ticketsystem.TicketController;
import ch.frily.xyzbot.ticketsystem.TicketStatus;
import ch.frily.xyzbot.ticketsystem.embeds.CloseAcceptedEmbed;
import ch.frily.xyzbot.ticketsystem.embeds.CloseRejectedEmbed;
import ch.frily.xyzbot.ticketsystem.embeds.ClosedOptionsEmbed;
import lombok.Setter;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class CloseRequestAcceptBtn implements IButton {

    @Setter
    private boolean disabled = false;

    @Override
    public String getId() {
        return "ticket-closerequest-accept-button";
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

        // Edit requestMessage
        CloseRequestAcceptBtn acceptBtn = new CloseRequestAcceptBtn();
        CloseRequestRejectBtn rejectBtn = new CloseRequestRejectBtn();
        acceptBtn.setDisabled(true);
        rejectBtn.setDisabled(true);

        CloseAcceptedEmbed acceptedEmbed = new CloseAcceptedEmbed();
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

        // Edit Channel
        event.getChannel().asTextChannel().getMemberPermissionOverrides().forEach(memberOverride -> {
            memberOverride.delete().queue();
        });
        TicketController.getInstance().changeTicketStatus(TicketStatus.CLOSED, event.getChannel().asTextChannel());

        // Send new rejectEmbed
        ClosedOptionsEmbed optionEmbed = new ClosedOptionsEmbed();
        optionEmbed.setMember(event.getMember());
        optionEmbed.setChannel(event.getChannel().asTextChannel());
        event.replyEmbeds(optionEmbed.build()).queue();
    }
}
