package ch.frily.xyzbot.feature.action;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import ch.frily.xyzbot.util.TicketStatus;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.SQLException;

import static ch.frily.xyzbot.feature.TicketManager.userIsTeammember;

@Slf4j
public class TicketClaimAction {

    /**
     *
     * @param channel
     * @param user
     * @return True if everything was executed properly / False if the channel is not a Ticketchannel.
     */
    public void execute(TextChannel channel, User user) throws SQLException, NotFoundException {

    }


}
