package ch.frily.xyzbot.utils;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.exceptions.InvalidKeyword;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

import javax.naming.InvalidNameException;
import java.util.Objects;

/**
 * Resolve Discord related IDs
 */
@Slf4j
public class IdResolver {

    /**
     * Get a Guild by its integer ID
     * @param guildId
     * @return Guild object
     */
    public static Guild getGuildById(long guildId) {
        return Client.getInstance().getClient().getGuildById(guildId);
    }

    /**
     * Get a Guild by its .env keyword
     * @param keyword In the .env-file defined keyword
     * @return Guild object
     */
    public static Guild getGuildById(String keyword) {
        long guildId = checkAndResolve(keyword, Long.class);
        return getGuildById(guildId);
    }

    /**
     * Get a Role by its integer ID
     * @param roleId
     * @return Role object
     */
    public static Role getRoleById(long roleId) {
        return Client.getInstance().getClient().getRoleById(roleId);
    }

    /**
     * Get a Channel by its .env keyword
     * @param keyword In the .env-file defined keyword
     * @return Channel object
     */
    public static Role getRoleById(String keyword) {
        long roleId = checkAndResolve(keyword, Long.class);
        return getRoleById(roleId);
    }

    /**
     * Get a Channel by its integer ID
     * @param channelId
     * @return  object
     */
    public static GuildChannel getChannelById(long guildId, long channelId) {
        Guild guild = getGuildById(guildId);
        GuildChannel channel = guild.getGuildChannelById(channelId);

        if (channel == null) {
            throw new NullPointerException("Kanal mit ID " + channelId + " nicht gefunden.");
        }

        return switch (channel) {
            case TextChannel t -> (TextChannel) channel;
            case VoiceChannel v -> (VoiceChannel) channel;
            default -> channel;
        };
    }

    /**
     * Get a Role by its .env keyword
     * @param guildKeyword In the .env-file defined guild keyword
     * @param channelKeyword In the .env-file defined channel keyword
     * @return Role object
     */
    public static GuildChannel getChannelById(String guildKeyword, String channelKeyword) {
        long guildId = checkAndResolve(guildKeyword, Long.class);
        long channelId = checkAndResolve(channelKeyword, Long.class);
        return getChannelById(guildId, channelId);
    }

    /**
     * Checks the keyword for empty or null value, and resolves the key
     * @param keyword
     * @return Resolved value
     * @param <T> Returntype of the resolved value
     */
    private static <T> T checkAndResolve(String keyword, Class<T> type) {
        if (Objects.equals(keyword, "")) {
            throw new IllegalArgumentException("Illegal keyword");
        }
        String value = Client.getInstance().getConfig().get(keyword);

        if (Objects.equals(value, "")) throw new IllegalStateException("Keyword is null");
        if (type == String.class) return type.cast(value);
        if (type == Integer.class) return type.cast(Integer.parseInt(value));
        if (type == Long.class)    return type.cast(Long.parseLong(value));
        if (type == Boolean.class) return type.cast(Boolean.parseBoolean(value));
        if (type == Double.class)  return type.cast(Double.parseDouble(value));

        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

}
