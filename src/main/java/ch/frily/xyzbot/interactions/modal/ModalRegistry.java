package ch.frily.xyzbot.interactions.modal;

import ch.frily.xyzbot.ticketsystem.panel.interaction.TypeSelectorModal;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ModalRegistry {

    private static ModalRegistry instance;

    private Map<String, IModal> modals = new HashMap<>();

    public static ModalRegistry getInstance(){
        if (instance == null) {
            instance = new ModalRegistry();
        }
        return instance;
    }

    public void loadModals(){
        List<IModal> rawModals = List.of(
                new TypeSelectorModal()
        );

        rawModals.forEach(modal -> {
            log.info("Loaded modal with id {}", modal.getId());
            modals.put(modal.getId(), modal);
        });
    }

    public void dispatchModalInteraction(ModalInteractionEvent event) {
        log.debug("Modal interaction dispatched: {}", event.getModalId());
        modals.get(event.getModalId()).execute(event);
    }
}
