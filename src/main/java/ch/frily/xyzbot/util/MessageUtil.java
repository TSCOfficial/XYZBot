package ch.frily.xyzbot.util;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.ActionComponent;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.actionrow.ActionRowChildComponent;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.label.LabelChildComponent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.components.actionrow.ActionRowImpl;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MessageUtil {

    /**
     * Enable or disable a list of Buttons
     * @param newState
     * @return
     */
    public static List<ActionComponent> toggleComponentDisableableState(List<ActionComponent> components, boolean newState){
        return components.stream().map(component -> {
            return component.withDisabled(newState);
        }).toList();
    }

    public static List<ActionRow> toggleAllMessageComponentDisableableState(Message message, boolean newState) {
        return message.getComponents().stream()
                .map(actionRow -> {
                    List<ActionRowChildComponent> newRow = actionRow.asActionRow().getComponents().stream().map(component -> {
                        log.debug(component.asButton().getLabel());
                        log.debug(component.getType().name());
                        return switch (component.getType()) {
                            case BUTTON -> component.asButton().withDisabled(newState);
                            case STRING_SELECT -> component.asStringSelectMenu().withDisabled(newState);
                            case USER_SELECT, ROLE_SELECT, CHANNEL_SELECT, MENTIONABLE_SELECT ->
                                    component.asEntitySelectMenu().withDisabled(newState);
                            default -> component;
                        };
                    }).toList();
                    Button testbtn = (Button) newRow.get(1);
                    log.debug(testbtn.getLabel());
                    log.debug("disabled: {}", testbtn.isDisabled());
                    return ActionRow.of(newRow);
                }).toList();
    }
}
