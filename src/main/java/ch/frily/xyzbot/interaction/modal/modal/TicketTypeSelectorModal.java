package ch.frily.xyzbot.interaction.modal.modal;

import ch.frily.xyzbot.interaction.modal.IModal;
import ch.frily.xyzbot.feature.TicketManager;
import ch.frily.xyzbot.feature.TicketType;
import ch.frily.xyzbot.feature.TicketTypeGroup;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Slf4j
public class TicketTypeSelectorModal implements IModal {

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
            selectMenuBuilder.addOption(type.getLabel(), type.getId(), type.getSelectDescription());
        });
        selectMenuBuilder.setMinValues(1);
        selectMenuBuilder.setMaxValues(1);
        selectMenuBuilder.setPlaceholder("Typ auswählen");

        components.put("Tickettyp", selectMenuBuilder.build());

        return components;
    }

    @Override
    public void execute(@NotNull ModalInteractionEvent event) {
        event.deferReply(true).queue();
        try {

            TicketType ticketType = Arrays.stream(TicketType.values()).filter(type ->
                    Objects.equals(type.getId(), event.getValue("select-menu:ticket-type-selector").getAsStringList().getFirst())
            ).findFirst().orElseThrow(() -> new IllegalStateException("Tickettyp ist ungültig."));

            TicketManager.getInstance().createTicket(ticketType, event.getMember(), channel -> {
                event.getHook().sendMessage("Dein Ticket wurde erstellt: " + channel.getAsMention()).queue();
            });
        } catch (PermissionException permissionException) {
            log.error(permissionException.getMessage());
            event.getHook().editOriginal(permissionException.getMessage()).queue();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
            event.getHook().editOriginal("Ein unbekannter Fehler ist aufgetaucht. Bitte versuche es später erneut.").queue();
        }
    }
}
