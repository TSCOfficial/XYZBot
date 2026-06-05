package ch.frily.xyzbot.feature.action;

import ch.frily.xyzbot.util.TicketStatus;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Objects;

import static ch.frily.xyzbot.feature.TicketController.isNewTicketchannel;
import static ch.frily.xyzbot.feature.TicketController.userIsTeammember;

@Slf4j
public class TicketClaimAction {

    /**
     *
     * @param channel
     * @param user
     * @return True if everything was executed properly / False if the channel is not a Ticketchannel.
     */
    public void execute(TextChannel channel, User user) {
        if (!isNewTicketchannel(channel) || !userIsTeammember(user)){
            return;
        };

        if (channel.getName().startsWith(TicketStatus.NEW.getIcon())) {
            String newChannelName = TicketStatus.CLAIMED.getIcon() + channel.getName().substring(TicketStatus.NEW.getIcon().length());
            channel.getManager().setName(newChannelName).queue();
        }

        channel.getManager().setTopic(
                channel.getTopic() + " | Fall übernommen von " + Objects.requireNonNull(user).getEffectiveName()
        ).queue();
    }


}
