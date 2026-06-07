package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.util.TicketStatus;
import ch.frily.xyzbot.util.TicketType;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ch.frily.xyzbot.feature.TicketManager.userIsTeammember;

public class Ticket {

    // minimal close-requests till ticket can be force-closed
    private static final int MIN_CLOSE_REQUEST_COUNT = 2;

    // minimal inactivity in days till ticket can be force-closed
    private static final int MIN_INACTIVITY_DURATION = 7;


    // Person who opened the Ticket
    @Getter
    private final Member owner;

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
    private final TicketType type;

    // When the last message was sent
    @Getter
    @Setter
    private LocalDateTime lastActivityAt;

    @Getter
    @Setter
    private Message welcomeMessage;

    @Getter
    private int closeRequestCount;

    @Getter
    private boolean isRequestPending;

    @Getter
    private TicketStatus status;

    public Ticket(Member owner, TicketType type){
        this.owner = owner;
        this.type = type;
        this.lastActivityAt = LocalDateTime.now();
        this.status = TicketStatus.NEW;
    }

    public long getId(){
        return channel.getIdLong();
    }


    public String getNameWithoutStatus(){
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

    public void setStatus(TicketStatus status) throws SQLException {
        this.status = status;
        channel.getManager().setName(status.getIcon() + this.getNameWithoutStatus()).queue();
        TicketController.updateTicket(this);
    }

    /**
     * Checks if the {@link TextChannel} is a NEW Ticket or nor
     * @return True if its a NEW Ticket / False if not
     */
    public boolean isNewTicket() {
        return status.equals(TicketStatus.NEW);
    }

    /**
     * Request a ticket closing
     * @throws SQLException
     * @throws IllegalStateException When no close request can be sent
     */
    public void requestClose() throws SQLException {
        if (this.isCloseRequestable()){
            this.closeRequestCount ++;
            this.isRequestPending = true;
            TicketController.updateTicket(this);
            return;
        }
        throw new IllegalStateException("In diesem Ticket kann keine Schliessanfrage gesendet werden.\n-# Das Ticket ist wohl bereits geschlossen?");
    }

    /**
     * Close a Ticket<br>
     * Removes user permissions, changes status, ...
     */
    public void close() throws SQLException {
        this.status = TicketStatus.CLOSED;

        channel.getMemberPermissionOverrides().forEach(memberOverride -> {
            memberOverride.delete().queue();
        });

        TicketController.updateTicket(this);
    }

    /**
     * Claims a ticket to assign it to someone
     * @param member
     * @return True if claimed successfully, false if the member is not qualified or the ticket can not be claimed
     */
    public void claim(Member member){
        if (assignee != null || !this.isNewTicket() || !userIsTeammember(member.getUser())){
            return;
        };

        this.status = TicketStatus.CLAIMED;

        this.updateChannelTopic();
    }

    public boolean isForceClosable(){
        return lastActivityAt.isBefore(LocalDateTime.now().minusDays(MIN_INACTIVITY_DURATION)) || closeRequestCount >= MIN_CLOSE_REQUEST_COUNT;
    }

    public boolean isOwner(Member initiator){
        return initiator.equals(owner);
    }

    private boolean isCloseRequestable(){
        return switch (status) {
            case NEW -> true;
            case CLAIMED -> true;
            case ARCHIVED -> false;
            case CLOSED -> false;
        };
    }

    public void updateChannelTopic(){
        String topic = type.getLabel();

        if (assignee != null) {
            topic += " | " + assignee.getEffectiveName();
        }
        channel.getManager().setTopic(topic).queue();
    }
}
