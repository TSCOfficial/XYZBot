package ch.frily.xyzbot.listeners;

import ch.frily.xyzbot.teamlist.Teamlist;
import ch.frily.xyzbot.utils.IdResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class RoleUpdateListener extends ListenerAdapter {

    private static RoleUpdateListener instance;

    public static RoleUpdateListener getInstance() {
        if (instance == null) {
            instance = new RoleUpdateListener();
        }
        return instance;
    }

    @Override
    public void onGuildMemberUpdate(GuildMemberUpdateEvent event) {
        log.info("Updated {}", event.getMember().getEffectiveName());

        MessageEmbed embed = Teamlist.getInstance().generateEmbed();

        ActionRow components = ActionRow.of(Button.link("https://discord.com/channels/719211950269005857/737440736529875035", "Bewerben"));
        TextChannel channel = (TextChannel) IdResolver.getChannelById(TextChannel.class, "GUILD_XYZCRAFT", "CHANNEL_TEAMLIST");


        IdResolver.getMessageById(event.getGuild().getIdLong(), channel.getIdLong(), channel.getLatestMessageIdLong()).thenAccept(message -> {
            message.editMessageEmbeds(embed).queue();
        }).exceptionally(error -> {
            channel.sendMessage("").addEmbeds(embed).addComponents(components).queue();
            return null;
        });

    }
}
