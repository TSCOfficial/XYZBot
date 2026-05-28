package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.List;

@Slf4j
public class TicketController {

    private static TicketController instance;

    private List<Permission> ownerPermissions = List.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND); // This is just in case! Other permissions are set on the category.

    public static TicketController getInstance() {
        if (instance == null) {
            instance = new TicketController();
        }
        return instance;
    }

    public void createTicket(Ticket ticket) {
        Category ticketCategory = EnvResolver.getCategoryById(EnvKey.CATEGORY_TICKETS);
        ChannelAction<TextChannel> ticketCreation = ticketCategory.createTextChannel(resolveName(ticket));
        ticketCreation.addMemberPermissionOverride(ticket.getOwner().getIdLong(), ownerPermissions, null).queue();

        ticketCreation.queue(textChannel -> {
            log.info("Ticket done");
        });
    }

    public String resolveName(Ticket ticket) {
        String type = ticket.getType().getId();
        String username = ticket.getOwner().getEffectiveName();
        return type + "-" + username;
    }

}
