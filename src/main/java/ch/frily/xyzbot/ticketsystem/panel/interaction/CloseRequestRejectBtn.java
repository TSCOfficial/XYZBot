package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import ch.frily.xyzbot.ticketsystem.actions.CloseRejectedEmbed;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class CloseRequestRejectBtn implements IButton {

    private static CloseRequestRejectBtn instance;

    public static CloseRequestRejectBtn getInstance(){
        if (instance == null) {
            instance = new CloseRequestRejectBtn();
        }
        return instance;
    }

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

        CloseRequestAcceptBtn acceptbtn = CloseRequestAcceptBtn.getInstance();
        acceptbtn.setDisabled(true);
        instance.setDisabled(true);
        log.debug("set up new buttons");
        CloseRejectedEmbed embed = new CloseRejectedEmbed();
        embed.setMember(event.getMember());
        embed.setChannel(event.getChannel().asTextChannel());

        log.debug("set up embed");

        event.editMessageEmbeds(embed.build())
                .setComponents(
                        ActionRow.of(
                                acceptbtn.build(),
                                instance.build()
                        )
                )
                .queue();
    }
}
