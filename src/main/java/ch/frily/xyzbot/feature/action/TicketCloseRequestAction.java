package ch.frily.xyzbot.feature.action;

import ch.frily.xyzbot.embed.TicketCloseRequestEmbed;
import ch.frily.xyzbot.interaction.button.btn.TicketCloseRequestAcceptBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketCloseRequestRejectBtn;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCloseRequestAction{

    public void execute(IReplyCallback event) {

        ActionRow actionrow = ActionRow.of(
                new TicketCloseRequestAcceptBtn().build(),
                new TicketCloseRequestRejectBtn().build()
        );

        TicketCloseRequestEmbed embed = new TicketCloseRequestEmbed();
        embed.setMember(event.getMember());
        embed.setChannel((TextChannel) event.getGuildChannel());
        event.replyEmbeds(embed.build()).addComponents(actionrow).queue();
    }
}
