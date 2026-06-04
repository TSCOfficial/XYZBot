package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TicketClaimAction {

    /**
     *
     * @param channel
     * @param user
     * @return True if everything was executed properly / False if the channel is not a Ticketchannel.
     */
    public boolean execute(TextChannel channel, User user) {
        if (!isChannelANewTicket(channel) || !userIsTeammember(user)){
            return false;
        };

        if (channel.getName().startsWith(TicketStatus.NEW.getIcon())) {
            String newChannelName = TicketStatus.CLAIMED.getIcon() + channel.getName().substring(TicketStatus.NEW.getIcon().length());
            channel.getManager().setName(newChannelName).queue();
        }

        channel.getManager().setTopic(
                channel.getTopic() + " | Fall übernommen von " + Objects.requireNonNull(user).getEffectiveName()
        ).queue();
        return true;
    }

    private boolean isChannelANewTicket(TextChannel channel) {
        log.debug(channel.getName());
        List<String> ticketTypeIds = Arrays.stream(TicketType.values())
                .map(TicketType::getId)
                .toList();

        return ticketTypeIds.stream().anyMatch(typeId -> {
            return channel.getName().startsWith(TicketStatus.NEW.getIcon() + typeId);
        });
    }

    private boolean userIsTeammember(User user) {
        Role teamRole = EnvResolver.getRoleById(EnvKey.ROLE_TEAM);
        return EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT).getMemberById(user.getId()).getRoles().contains(teamRole);
    }
}
