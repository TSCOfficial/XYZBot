package ch.frily.xyzbot.utils;

import ch.frily.xyzbot.Client;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

/**
 * Resolve Discord related IDs
 */
public class IdResolver {

    /**
     * Get a Guild by its integer ID
     * @param guildId
     * @return Guild object
     */
    public static Guild getGuildById(int guildId) {
        return Client.getInstance().getClient().getGuildById(guildId);
    }

    /**
     * Get a Guild by its .env Keyword
     * @param keyword In the .env-file defined keyword
     * @return Guild object
     */
    public static Guild getGuildById(String keyword) {
        int guildId = Integer.parseInt(Client.getInstance().getConfig().get(keyword));
        return getGuildById(guildId);
    }

}
