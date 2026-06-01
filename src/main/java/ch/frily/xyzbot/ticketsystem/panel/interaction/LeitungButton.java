package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import ch.frily.xyzbot.ticketsystem.TicketTypeGroup;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class LeitungButton implements IButton {

    private static LeitungButton instance;

    public static LeitungButton getInstance(){
        if (instance == null) {
            instance = new LeitungButton();
        }
        return instance;
    }

    @Override
    public String getId() {
        return "ticket-leitung";
    }

    @Override
    public String getLabel() {
        return "Leitung";
    }

    @Override
    public ButtonStyle getStyle() {
        return ButtonStyle.SECONDARY;
    }

    @Override
    public void execute(@NotNull ButtonInteractionEvent event) {
        TypeSelectorModal modal = new TypeSelectorModal();
        modal.setTypeGroup(TicketTypeGroup.LEITUNG);
        event.replyModal(modal.build()).queue();
    }
}
