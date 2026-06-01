package ch.frily.xyzbot.interactions.modal;

import net.dv8tion.jda.api.components.ModalTopLevelComponent;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface IModal {

    String getTitle();

    String getId();

    /**
     * Component tree with Label & component
     * @return
     */
    Map<String, LabelChildComponent> getComponents();

    default Modal build() {

        Modal.Builder modalBuilder = Modal.create(getId(), getTitle());
        modalBuilder.addComponents(
                getComponents().entrySet().stream().map(entry -> {
                    return Label.of(entry.getKey(), entry.getValue());
                }).toList()
        );
        return modalBuilder.build();
    }

    void execute(@NotNull ModalInteractionEvent event);
}
