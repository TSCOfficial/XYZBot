package ch.frily.xyzbot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

@Slf4j
@UtilityClass
public final class Util {

    public static String format(String template, Object... args) {
        int i = 0;
        while (template.contains("{}") && i < args.length) {
            template = template.replaceFirst("\\{\\}", String.valueOf(args[i++]));
        }
        return template;
    }

    public static String formatDateTime(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        return instant.atZone(ZoneId.of("Europe/Paris")).format(formatter);
    }

    public static String calcDuration(Temporal startInclusive, Temporal endExclusive){
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
