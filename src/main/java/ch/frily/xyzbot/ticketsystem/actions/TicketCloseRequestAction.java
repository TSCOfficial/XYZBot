package ch.frily.xyzbot.ticketsystem.actions;

import ch.frily.xyzbot.ticketsystem.panel.interaction.CloseRequestAcceptBtn;
import ch.frily.xyzbot.ticketsystem.panel.interaction.CloseRequestRejectBtn;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class TicketCloseRequestAction extends TicketAction{
    @Override
    public void execute(IReplyCallback event) {

        ActionRow actionrow = ActionRow.of(
                CloseRequestAcceptBtn.getInstance().build(),
                CloseRequestRejectBtn.getInstance().build()
        );

        CloseRequestEmbed embed = new CloseRequestEmbed();
        embed.setMember(event.getMember());
        embed.setChannel((TextChannel) event.getGuildChannel());
        event.replyEmbeds(embed.build()).addComponents(actionrow).queue();
    }
}
