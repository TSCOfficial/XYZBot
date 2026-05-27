package ch.frily.xyzbot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class OnReadyListener extends ListenerAdapter {

    private static OnReadyListener instance;

    public static OnReadyListener getInstance() {
        if (instance == null) {
            instance = new OnReadyListener();
        }
        return instance;
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        log.info("Loading members from {}.", event.getGuild().getName());
        event.getGuild().loadMembers().onSuccess(members -> {
            log.debug("Loaded all members from {}: {}", event.getGuild().getName(), members.size());
        });
    }

    @Override
    public void onReady(ReadyEvent event) {
        log.debug("ON READY");
    }
}
