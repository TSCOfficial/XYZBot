package ch.frily.xyzbot.ticketsystem.actions;

import ch.frily.xyzbot.ticketsystem.TicketStatus;
import ch.frily.xyzbot.ticketsystem.TicketType;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class TicketAction {

    public void execute(TextChannel channel, User user){
        throw new RuntimeException("Action was not defined");
    }

    public void execute(IReplyCallback event){
        throw new RuntimeException("Action with argument IReplyCallback was not defined");
    }

    String getTicketNameWithoutStatus(TextChannel channel){
        List<String> statusIcons = Arrays.stream(TicketStatus.values())
                .map(TicketStatus::getIcon)
                .toList();

        String channelName = channel.getName();

        for (String icon : statusIcons) {
            if (channelName.startsWith(icon)) {
                channelName = channelName.substring(icon.length());
                break;
            }
        }

        return channelName;
    }

    /**
     * Checks if the {@link TextChannel} is a Ticket or nor
     * @param channel
     * @return True if its a Ticketchannel / False if not
     */
    boolean isTicketchannel(TextChannel channel) {
        List<String> ticketTypeIds = Arrays.stream(TicketType.values())
                .map(TicketType::getId)
                .toList();

        String nameWithoutStatus = getTicketNameWithoutStatus(channel);
        return ticketTypeIds.stream().anyMatch(nameWithoutStatus::startsWith);
    }

    /**
     * Checks if the {@link TextChannel} is a NEW Ticket or nor
     * @param channel
     * @return True if its a NEW Ticket / False if not
     */
    boolean isNewTicketchannel(TextChannel channel) {
        List<String> ticketTypeIds = Arrays.stream(TicketType.values())
                .map(TicketType::getId)
                .toList();

        return ticketTypeIds.stream().anyMatch(typeId -> {
            return channel.getName().startsWith(TicketStatus.NEW.getIcon() + typeId);
        });
    }

    /**
     * Checks if the user is a Teammember or nor
     * @param user
     * @return True if its a Teammember / False if not
     */
    boolean userIsTeammember(User user) {
        Role teamRole = EnvResolver.getRoleById(EnvKey.ROLE_TEAM);
        return EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT).getMemberById(user.getId()).getRoles().contains(teamRole);
    }
}
