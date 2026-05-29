package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.awt.*;
import java.util.Date;
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

        // Ticket settings
        ChannelAction<TextChannel> ticketCreation = ticketCategory.createTextChannel(ticket.getTicketName());
        ticketCreation.addMemberPermissionOverride(ticket.getOwner().getIdLong(), ownerPermissions, null).queue();
        ticketCreation.setTopic(ticket.getType().getLabel()).queue();

        // Ticket content
        ticketCreation.queue(textChannel -> {

            textChannel.sendMessage(ticket.getOwner().getAsMention() + ticket.getType().getMentions())
                    .addEmbeds(createEmbed(ticket)).queue();
        });
    }

    private MessageEmbed createEmbed(Ticket ticket) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(ticket.getType().getLabel());
        String description = "Willkommen " + ticket.getOwner().getAsMention() + "!" +
                "\n" +
                ticket.getType().getDescription();
        embed.setDescription(description);
        embed.setFooter(ticket.getTicketName());
        embed.setTimestamp(new Date().toInstant());
        embed.setColor(new Color(46, 204, 113));
        return embed.build();
    }

}
