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
     * Enable/Disable a list of components
     * @param components
     * @param newState
     * @return
     */
    private static List<ActionComponent> toggleComponentDisableableState(List<ActionComponent> components, boolean newState){
        return components.stream().map(component -> {
            return component.withDisabled(newState);
        }).toList();
    }

    /**
     * Enable/Disable all components of a message
     * @param message
     * @param withDisabled
     * @return
     */
    private static List<ActionRow> toggleAllMessageComponentDisableableState(Message message, boolean withDisabled) {
        return message.getComponents().stream()
                .map(actionRow -> {
                    return actionRow.asActionRow().withDisabled(withDisabled);
                }).toList();
    }

    public static List<ActionRow> disableAllMessageComponents(Message message){
        return toggleAllMessageComponentDisableableState(message, true);
    }

    public static List<ActionRow> enableAllMessageComponents(Message message){
        return toggleAllMessageComponentDisableableState(message, false);
    }
}
