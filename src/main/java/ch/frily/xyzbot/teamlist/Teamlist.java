package ch.frily.xyzbot.teamlist;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.utils.IdResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class Teamlist {

    private static final List<String> ROLE_IDS = List.of(
            Client.getInstance().getConfig().get("ROLE_LEITUNG"),
            Client.getInstance().getConfig().get("ROLE_ENTWICKLUNG"),
            Client.getInstance().getConfig().get("ROLE_PROBEENTWICKLUNG"),
            Client.getInstance().getConfig().get("ROLE_MODERATION"),
            Client.getInstance().getConfig().get("ROLE_SUPPORT"),
            Client.getInstance().getConfig().get("ROLE_BAUTRUPP"),
            Client.getInstance().getConfig().get("ROLE_PROBEBAUTRUPP"),
            Client.getInstance().getConfig().get("ROLE_GESTALTUNG")
    );

    private void generateFields() {
        ROLE_IDS.forEach((id) -> {

        });
    }

    private void generateFieldByRole(Role role) {

    }

    private List<Member> getUsersByRole(int roleId) {
        Guild guild = IdResolver.getGuildById("GUILD_XYZCRAFT");
        Role role = Client.getInstance().getClient().getRoleById(roleId);

        return guild.getMembersWithRoles(role);
    }
}
