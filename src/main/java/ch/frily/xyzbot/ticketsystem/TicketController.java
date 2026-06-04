package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public void createTicket(TicketType type, Member ticketOwner, Consumer<TextChannel> onCreated) {

        // check if permitted: Bewerbung-support -> Has linked account with minecraft?
        isPermitted(type, ticketOwner);

        Ticket ticket = new Ticket();
        ticket.setType(type);
        ticket.setOwner(ticketOwner);

        Category ticketCategory = EnvResolver.getCategoryById(EnvKey.CATEGORY_TICKETS);
        ActionRow actionrow = ActionRow.of(List.of());
        // Ticket settings
        ticketCategory.createTextChannel(generateTicketName(type, ticketOwner))
                .addMemberPermissionOverride(ticketOwner.getIdLong(), ownerPermissions, null)
                .setTopic(type.getLabel())
                .queue(textChannel -> {
                    // Ticket content
                    ticket.setChannel(textChannel);
                    textChannel.sendMessage(ticketOwner.getAsMention() + " - " + type.getResponsibleRoles().stream().map(Role::getAsMention).collect(Collectors.joining(", ")))
                            .addEmbeds(createEmbed(ticket)).setComponents(actionrow).queue();
                    onCreated.accept(textChannel);
        });
    }

    private MessageEmbed createEmbed(Ticket ticket) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(ticket.getType().getLabel());
        String description = "Willkommen **" + ticket.getOwner().getUser().getGlobalName() + "**!" +
                "\n" +
                ticket.getType().getEmbedDescription();
        embed.setDescription(description);
        embed.setFooter(ticket.getChannel().getName());
        embed.setTimestamp(new Date().toInstant());
        embed.setColor(ticket.getOwner().getColors().getPrimary());
        return embed.build();
    }

    private String generateTicketName(TicketType type, Member ticketOwner) {
        String status = TicketStatus.NEW.getIcon();
        String typeId = type.getId();
        String username = ticketOwner.getUser().getEffectiveName();
        int randInt = ThreadLocalRandom.current().nextInt(100000, 999999);
        return status + typeId + "-" + username  + "-" + randInt;
    }

    /**
     * Check if the ticket owner is permitted or not
     * @param ticketOwner
     */
    private void isPermitted(TicketType type, Member ticketOwner){
        if (type == TicketType.BEWERBUNG_SUPPORT || type == TicketType.BEWERBUNG_EVENTTEAM) { // sup + event bewerbungen temporär gesperrt, bis das ganze System steht
            throw new PermissionException("Du bist nicht berechtigt dies auszuführen");
        }

    }

}
