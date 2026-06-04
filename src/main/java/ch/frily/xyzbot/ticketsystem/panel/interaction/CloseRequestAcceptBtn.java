package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.button.IButton;
import lombok.Setter;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class CloseRequestAcceptBtn implements IButton {

    private static CloseRequestAcceptBtn instance;

    public static CloseRequestAcceptBtn getInstance(){
        if (instance == null) {
            instance = new CloseRequestAcceptBtn();
        }
        return instance;
    }

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

    }
}
