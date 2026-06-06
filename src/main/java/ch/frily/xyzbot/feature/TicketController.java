package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.database.DatabaseQuery;
import ch.frily.xyzbot.database.Table;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import ch.frily.xyzbot.util.TicketType;
import javassist.NotFoundException;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketController {

    /**
     * Fetch a Ticket from the database.
     * @param id Ticket id (represented by the Ticket-Channel-ID)
     * @return The instance of this Ticket
     * @throws SQLException
     */
    public static Ticket getTicketById(long id) throws SQLException, NotFoundException {
        Ticket ticket = new Ticket();
        ResultSet resultSet = new DatabaseQuery(Table.TICKET)
                .select()
                .where(Table.TicketColumn.ID, DatabaseQuery.Operator.EQUALS, id).executeQuery();

        if (resultSet.getFetchSize() == 0) {
            throw new NotFoundException("Ticket mir ID " + id + " wurde nicht gefunden.");
        }
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

    public static Ticket updateCloseRequestCount(int newValue){

    }
}
