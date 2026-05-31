package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.interactions.button.IButton;
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
        // Show modal to select further
        event.reply("*Interaction received. Feature incomplete.*").setEphemeral(true).queue();
    }
}
