package ch.frily.xyzbot.ticketsystem.panel.interaction;

import ch.frily.xyzbot.interactions.modal.IModal;
import ch.frily.xyzbot.ticketsystem.TicketType;
import ch.frily.xyzbot.ticketsystem.TicketTypeGroup;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.components.ModalTopLevelComponent;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeSelectorModal implements IModal {

    @Setter
    private TicketTypeGroup typeGroup;

    @Override
    public String getTitle() {
        return "Ticketsupport";
    }

    @Override
    public String getId() {
        return "modal:ticket-type-modal";
    }

    @Override
    public Map<String, LabelChildComponent> getComponents() {
        Map<String, LabelChildComponent> components = new HashMap<>();
        List<TicketType> types = Arrays.stream(TicketType.values()).filter(type -> type.getGroup() == typeGroup).toList();

        StringSelectMenu.Builder selectMenuBuilder = StringSelectMenu.create("select-menu:ticket-type-selector");
        types.forEach(type -> {
            selectMenuBuilder.addOption(type.getLabel(), type.getId(), type.getDescription());
        });
        selectMenuBuilder.setMinValues(1);
        selectMenuBuilder.setMaxValues(1);
        selectMenuBuilder.setPlaceholder("Typ auswählen");

        components.put("Tickettyp", selectMenuBuilder.build());

        return components;
    }

    @Override
    public void execute(@NotNull ModalInteractionEvent event) {
        event.reply("Creating Ticket").setEphemeral(true).queue();
    }
}
