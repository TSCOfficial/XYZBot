package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.database.DatabaseQuery;
import ch.frily.xyzbot.database.Table;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import ch.frily.xyzbot.util.TicketType;
import javassist.NotFoundException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketController {

    /**
     * Fetch a Ticket from the database.
     * @param id Ticket id (represented by the Ticket-Channel-ID)
     * @return The instance of this Ticket
     * @throws SQLException When the database is unreachable
     * @throws NotFoundException When no ticket with given ID exist
     */
    public static Ticket getTicketById(long id) throws SQLException, NotFoundException {
        ResultSet resultSet = new DatabaseQuery(Table.TICKET)
                .select()
                .where(Table.TicketColumn.ID, DatabaseQuery.Operator.EQUALS, id).executeArchitectureQuery();

        resultSet.next();

        long ownerId = resultSet.getLong(Table.TicketColumn.OWNER_ID.getColumn());
        long assigneeId = resultSet.getLong(Table.TicketColumn.ASSIGNEE_ID.getColumn());
        long channelId = resultSet.getLong(Table.TicketColumn.CHANNEL_ID.getColumn());
        String typeString = resultSet.getString(Table.TicketColumn.TYPE.getColumn());
        Guild guild = EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT);

        Member owner = guild.getMemberById(ownerId);
        TicketType type = TicketType.valueOf(typeString);
        Ticket ticket = new Ticket(owner, type);
        ticket.setAssignee(guild.getMemberById(assigneeId));
        ticket.setChannel(guild.getTextChannelById(channelId));
        return ticket;
    }

    public static void createTicket(Ticket ticket) throws SQLException {
        DatabaseQuery query = new DatabaseQuery(Table.TICKET);
        query.insert(Table.TicketColumn.ID, ticket.getId());
        query.insert(Table.TicketColumn.OWNER_ID, ticket.getOwner().getIdLong());
        query.insert(Table.TicketColumn.CHANNEL_ID, ticket.getChannel().getIdLong());
        query.insert(Table.TicketColumn.TYPE, ticket.getType().name());
        query.insert(Table.TicketColumn.WELCOME_MESSAGE_ID, ticket.getWelcomeMessage().getIdLong());
        query.executeDataQuery();
    }

    public static void updateTicket(Ticket ticket) throws SQLException {
        DatabaseQuery query = new DatabaseQuery(Table.TICKET);

        if (ticket.getAssignee() != null) {
            query.update(Table.TicketColumn.ASSIGNEE_ID, ticket.getAssignee().getId());
        }
        query.update(Table.TicketColumn.LAST_ACTIVITY_AT, ticket.getLastActivityAt());
        query.update(Table.TicketColumn.IS_REQUEST_PENDING, ticket.isRequestPending());
        query.update(Table.TicketColumn.CLOSE_REQUEST_COUNT, ticket.getCloseRequestCount());
        query.executeDataQuery();
    }
}
