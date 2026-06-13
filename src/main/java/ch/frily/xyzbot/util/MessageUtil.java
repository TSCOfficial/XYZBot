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

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
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

    public static String format(String template, Object... args) {
        int i = 0;
        while (template.contains("{}") && i < args.length) {
            template = template.replaceFirst("\\{\\}", String.valueOf(args[i++]));
        }
        return template;
    }

    public static String formatTime(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formattedDate = instant.atOffset(ZoneOffset.ofTotalSeconds(0)).format(formatter);
        return formattedDate;
    }

    public static String duration(Temporal startInclusive, Temporal endExclusive){
        Duration duration = Duration.between(startInclusive, endExclusive);

        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        String openDuration;
        if (days > 0) {
            openDuration = String.format("%dd %dh %dmin", days, hours, minutes);
        } else if (hours > 0) {
            openDuration = String.format("%dh %dmin", hours, minutes);
        } else {
            openDuration = String.format("%dmin", minutes);
        }

        return openDuration;
    }
}
