package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.database.DatabaseQuery;
import ch.frily.xyzbot.database.Table;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import ch.frily.xyzbot.util.TicketStatus;
import ch.frily.xyzbot.util.TicketType;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Ticket {

    // Person who opened the Ticket
    @Getter
    @Setter
    private Member owner;

    // Team member that is assigned to this ticket
    @Getter
    @Setter
    private Member assignee;

    // The ticket channel itself
    @Getter
    @Setter
    private TextChannel channel;

    // Ticket type
    @Getter
    @Setter
    private TicketType type;

    // When the ticket was opened
    @Getter
    private LocalDateTime createdAt;

    // When the last message was sent
    @Getter
    private LocalDateTime lastActivityAt;

    public long getId(){
        return channel.getIdLong();
    }

    /**
     * Fetch a Ticket from the database.
     * @param id Ticket id (represented by the Ticket-Channel-ID)
     * @return The instance of this Ticket
     * @throws SQLException
     */
    public static Ticket getFromDatabase(long id) throws SQLException {
        Ticket ticket = new Ticket();
        ResultSet resultSet = new DatabaseQuery(Table.TICKET)
                .select()
                .where(Table.TicketColumn.ID, DatabaseQuery.Operator.EQUALS, id).executeQuery();

        resultSet.next();

        long ownerId = resultSet.getLong("owner_id");
        long assigneeId = resultSet.getLong("assignee_id");
        long channelId = resultSet.getLong("channel_id");
        String type = resultSet.getString("type");
        Guild guild = EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT);

        ticket.setOwner(guild.getMemberById(ownerId));
        ticket.setAssignee(guild.getMemberById(assigneeId));
        ticket.setChannel(guild.getTextChannelById(channelId));
        ticket.setType(TicketType.valueOf(type));
        return ticket;
    }


    public static String getTicketNameWithoutStatus(TextChannel channel){
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

    public static void changeTicketStatus(TicketStatus newStatus, TextChannel channel){
        channel.getManager().setName(newStatus.getIcon() + getTicketNameWithoutStatus(channel)).queue();
    }

    /**
     * Checks if the {@link TextChannel} is a NEW Ticket or nor
     * @return True if its a NEW Ticket / False if not
     */
    public static boolean isNewTicket(TextChannel channel) {
        List<String> ticketTypeIds = Arrays.stream(TicketType.values())
                .map(TicketType::getId)
                .toList();

        return ticketTypeIds.stream().anyMatch(typeId -> {
            return channel.getName().startsWith(TicketStatus.NEW.getIcon() + typeId);
        });
    }

    public void close(Member initiator){
        changeTicketStatus(TicketStatus.CLOSED, channel);
    }
}
