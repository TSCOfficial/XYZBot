package ch.frily.xyzbot.listener;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.feature.Teamlist;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class OnGuildMemberUpdate extends ListenerAdapter {

    private static OnGuildMemberUpdate instance;

    public static OnGuildMemberUpdate getInstance() {
        if (instance == null) {
            instance = new OnGuildMemberUpdate();
        }
        return instance;
    }

    @Override
    public void onGuildMemberUpdate(GuildMemberUpdateEvent event) {
        log.info("Updated {}", event.getMember().getEffectiveName());

        MessageEmbed embed = Teamlist.getInstance().generateEmbed();

        ActionRow components = ActionRow.of(
                Button.link("https://discord.com/channels/719211950269005857/737440736529875035", "Bewerben"),
                Button.link(Client.getInstance().getConfig().get("DOCMOST_AUFBAUORGA"), "Aufbauorganisation"));
        TextChannel channel = (TextChannel) EnvResolver.getChannelById(TextChannel.class, EnvKey.GUILD_XYZCRAFT, EnvKey.CHANNEL_DASTEAM);


        EnvResolver.getMessageById(event.getGuild().getIdLong(), channel.getIdLong(), channel.getLatestMessageIdLong()).thenAccept(message -> {
            message.editMessageEmbeds(embed).queue();
        }).exceptionally(error -> {
            channel.sendMessage("").addEmbeds(embed).addComponents(components).queue();
            return null;
        });

    }
}
