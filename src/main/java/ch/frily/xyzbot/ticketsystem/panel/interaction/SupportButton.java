package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import ch.frily.xyzbot.interactions.modal.IModal;
import ch.frily.xyzbot.ticketsystem.TicketTypeGroup;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class SupportButton implements IButton {

    private static SupportButton instance;

    public static SupportButton getInstance(){
        if (instance == null) {
            instance = new SupportButton();
        }
        return instance;
    }

    @Override
    public String getId() {
        return "ticket-support";
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
        TypeSelectorModal modal = new TypeSelectorModal();
        modal.setTypeGroup(TicketTypeGroup.SUPPORT);
        event.replyModal(modal.build()).queue();
    }
}
