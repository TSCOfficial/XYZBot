package ch.frily.xyzbot.interaction.button.btn;

import ch.frily.xyzbot.interaction.button.IButton;
import ch.frily.xyzbot.util.TicketTypeGroup;
import ch.frily.xyzbot.interaction.modal.modal.TicketTypeSelectorModal;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
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
        TicketTypeSelectorModal modal = new TicketTypeSelectorModal();
        modal.setTypeGroup(TicketTypeGroup.SUPPORT);
        event.replyModal(modal.build()).queue();
    }
}
