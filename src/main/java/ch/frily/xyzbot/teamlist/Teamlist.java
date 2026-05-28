package ch.frily.xyzbot.teamlist;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.utils.IdResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class Teamlist {

    private static Teamlist instance;

    private static final List<String> ROLE_KEYWORDS = List.of(
            "ROLE_LEITUNG",
            "ROLE_ENTWICKLUNG",
            "ROLE_PROBEENTWICKLUNG",
            "ROLE_MODERATION",
            "ROLE_SUPPORT",
            "ROLE_PROBESUPPORT",
            "ROLE_BAUTRUPP",
            "ROLE_PROBEBAUTRUPP",
            "ROLE_GESTALTUNG"
    );

    private static final Pattern ROLE_NAME_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\-\\s]");

    private List<Member> members = new ArrayList<>();

    public static Teamlist getInstance() {
        if (instance == null) {
            instance = new Teamlist();
        }
        return instance;
    }

    /**
     * Generate all the Fields sing the role keyword list
     */
    public MessageEmbed generateEmbed() {
        List<Role> roles = ROLE_KEYWORDS.stream().map(IdResolver::getRoleById).toList();
        List<MessageEmbed.Field> fields = roles.stream().map(this::generateFieldByRole).toList();

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("XYZCraft Team");
        fields.forEach(embedBuilder::addField);
        embedBuilder.setFooter("Zuletzt aktualisiert am");
        embedBuilder.setTimestamp(new Date().toInstant());
        embedBuilder.setColor(new Color(46, 204, 113));
        fillWithBlankFields(embedBuilder);

        List<Member> teammembers = getUsersByRole(IdResolver.getRoleById("ROLE_TEAM"));
        embedBuilder.addField("Gesammtes Team (" + teammembers.size() + ")",
                                teammembers.stream().map(Member::getAsMention).collect(Collectors.joining(", ")), false);

        return embedBuilder.build();
    }

    /**
     * Fill the embed with blank fields to keep the embed structured no matter how many roles there are.
     */
    private void fillWithBlankFields(EmbedBuilder embedBuilder) {
        int fieldCountOffset = embedBuilder.getFields().size() % 3;
        if (fieldCountOffset != 0) {
            int blanksNeeded = 3 - fieldCountOffset;
            for (int i = 0; i < blanksNeeded; i++) {
                embedBuilder.addBlankField(true);
            }
        }
    }

    /**
     * Generate a field by a given Role
     * @param role
     */
    private MessageEmbed.Field generateFieldByRole(Role role) {
        String userList = "*Bewirb dich!*";
        int userCount = 0;

        if (!getUsersByRole(role).isEmpty()) {
            userList = getUsersByRole(role).stream().map(Member::getAsMention).collect(Collectors.joining("\n"));
            userCount = getUsersByRole(role).size();
        }
        return new MessageEmbed.Field(
                extractText(role.getName()) + " (" + userCount + ")",
                role.getAsMention() + "\n" + userList,
                true
        );
    }

    /**
     * Find members that have a given role<br>
     * @param role
     * @return true | false : Returns true when the list is completed
     */
    private List<Member> getUsersByRole(Role role) {
        Guild guild = IdResolver.getGuildById("GUILD_XYZCRAFT");
        return guild.getMembersWithRoles(role);
    }

    private static String extractText(String input) {
        if (input == null || input.isBlank()) return input;
        return ROLE_NAME_PATTERN.matcher(input).replaceAll("").trim();
    }
}
