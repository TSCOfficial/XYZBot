package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import ch.frily.xyzbot.ticketsystem.TicketTypeGroup;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class BewerbungButton implements IButton {

    private static BewerbungButton instance;

    public static BewerbungButton getInstance(){
        if (instance == null) {
            instance = new BewerbungButton();
        }
        return instance;
    }

    @Override
    public String getId() {
        return "ticket-bewerbung";
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
        TypeSelectorModal modal = new TypeSelectorModal();
        modal.setTypeGroup(TicketTypeGroup.BEWERBUNG);
        event.replyModal(modal.build()).queue();
    }
}
