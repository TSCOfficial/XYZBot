package ch.frily.xyzbot.ticketsystem.actions;

import ch.frily.xyzbot.ticketsystem.TicketStatus;
import ch.frily.xyzbot.ticketsystem.TicketType;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class TicketAction {

    public void execute(TextChannel channel, User user){
        throw new RuntimeException("Action was not defined");
    }

    public void execute(IReplyCallback event){
        throw new RuntimeException("Action with argument IReplyCallback was not defined");
    }
}
