package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.database.DatabaseQuery;
import ch.frily.xyzbot.database.Table;
import ch.frily.xyzbot.embed.TicketCloseRequestEmbed;
import ch.frily.xyzbot.embed.TicketClosedOptionsEmbed;
import ch.frily.xyzbot.interaction.button.btn.TicketArchiveBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketCloseRequestAcceptBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketCloseRequestRejectBtn;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import ch.frily.xyzbot.util.TicketStatus;
import ch.frily.xyzbot.util.TicketType;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Ticket {

    // minimal close-requests till ticket can be force-closed
    private static final int MIN_CLOSE_REQUEST_COUNT = 2;

    // minimal inactivity in days till ticket can be force-closed
    private static final int MIN_INACTIVITY_DURATION = 7;


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

    // When the last message was sent
    @Getter
    private LocalDateTime lastActivityAt;

    @Getter
    private int closeRequestCount;

    public long getId(){
        return channel.getIdLong();
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

    public void changeTicketStatus(TicketStatus newStatus){
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

    /**
     * Team members can request to close a ticket. Each request adds +1 to its requestCount. After the second request, the team member can force-{@link #close} the ticket
     * @param event
     */
    public void requestClose(){

    }

    /**
     * Close a Ticket<br>
     * Removes user permissions, changes status, ...
     */
    public void close(){

        changeTicketStatus(TicketStatus.CLOSED);

        channel.getMemberPermissionOverrides().forEach(memberOverride -> {
            memberOverride.delete().queue();
        });

    }

    public boolean isForceClosable(){
        return lastActivityAt.isBefore(LocalDateTime.now().minusDays(MIN_INACTIVITY_DURATION)) || closeRequestCount >= MIN_CLOSE_REQUEST_COUNT;
    }
}
