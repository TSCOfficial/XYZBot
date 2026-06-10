package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.feature.TicketTypeGroup;
import ch.frily.xyzbot.interaction.modal.modal.TicketTypeSelectorModal;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class TicketPanelSupportBtn implements IButton {

    @Override
    public String getId() {
        return "ticket-panel-support-btn";
    }

    @Override
    public String getLabel() {
        return "Support";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.PRIMARY;
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        TicketTypeSelectorModal modal = new TicketTypeSelectorModal();
        modal.setTypeGroup(TicketTypeGroup.SUPPORT);
        event.replyModal(modal.build()).queue();
    }
}
