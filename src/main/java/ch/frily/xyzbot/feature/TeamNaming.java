package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.util.Util;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Adds the Icon of the top role to the end of a team member's name
 */
@Slf4j
public class TeamNaming {

    public static String nameMemberByTopRole(Member member) {
        AtomicReference<String> emoji = new AtomicReference<>("");
        member.getRoles().forEach(role -> {
            log.debug(role.getName());
            if (Util.hasEmoji(role.getName())) {
                log.debug("Found emoji");
                emoji.set(Util.getEmojis(role.getName()).group(0));
                return;
            }
        });
        return emoji.get();
    }
}
