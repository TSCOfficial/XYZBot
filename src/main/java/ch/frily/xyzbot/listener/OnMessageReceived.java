package ch.frily.xyzbot.listener;

import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketRepository;
import ch.frily.xyzbot.feature.TicketManager;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@Slf4j
public class OnMessageReceived extends ListenerAdapter {

    private static OnMessageReceived instance;

    public static OnMessageReceived getInstance(){
        if (instance == null) {
            instance = new OnMessageReceived();
        }
        return instance;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        if (TicketManager.getInstance().isTicketchannel(event.getChannel().asTextChannel())){
            Ticket ticket = TicketRepository.getTicketById(event.getChannel().getIdLong());
            ticket.claim(event.getMember());
        }
    }


}
