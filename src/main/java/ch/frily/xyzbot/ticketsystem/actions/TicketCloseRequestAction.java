package ch.frily.xyzbot.ticketsystem.actions;

import ch.frily.xyzbot.ticketsystem.embeds.CloseRequestEmbed;
import ch.frily.xyzbot.ticketsystem.panel.interaction.CloseRequestAcceptBtn;
import ch.frily.xyzbot.ticketsystem.panel.interaction.CloseRequestRejectBtn;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCloseRequestAction extends TicketAction{
    @Override
    public void execute(IReplyCallback event) {

        ActionRow actionrow = ActionRow.of(
                new CloseRequestAcceptBtn().build(),
                new CloseRequestRejectBtn().build()
        );

        CloseRequestEmbed embed = new CloseRequestEmbed();
        embed.setMember(event.getMember());
        embed.setChannel((TextChannel) event.getGuildChannel());
        event.replyEmbeds(embed.build()).addComponents(actionrow).queue();
    }
}
