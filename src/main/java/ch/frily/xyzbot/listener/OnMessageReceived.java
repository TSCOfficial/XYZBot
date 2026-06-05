package ch.frily.xyzbot.listener;

import ch.frily.xyzbot.feature.action.TicketClaimAction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
        log.debug("Message received");
        new TicketClaimAction().execute(event.getChannel().asTextChannel(), event.getAuthor());
    }


}
