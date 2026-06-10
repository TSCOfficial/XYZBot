package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.feature.TicketTypeGroup;
import ch.frily.xyzbot.interaction.modal.modal.TicketTypeSelectorModal;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class TicketPanelBewerbungButton implements IButton {

    @Override
    public String getId() {
        return "ticket-panel-bewerbung-btn";
    }

    @Override
    public String getLabel() {
        return "Bewerbung";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.SUCCESS;
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        TicketTypeSelectorModal modal = new TicketTypeSelectorModal();
        modal.setTypeGroup(TicketTypeGroup.BEWERBUNG);
        event.replyModal(modal.build()).queue();
    }
}
