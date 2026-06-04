package ch.frily.xyzbot.listeners;

import ch.frily.xyzbot.ticketsystem.actions.TicketClaimAction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class MessageSendListener extends ListenerAdapter {

    private static MessageSendListener instance;

    public static MessageSendListener getInstance(){
        if (instance == null) {
            instance = new MessageSendListener();
        }
        return instance;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        log.debug("Message received");
        new TicketClaimAction().execute(event.getChannel().asTextChannel(), event.getAuthor());
    }


}
