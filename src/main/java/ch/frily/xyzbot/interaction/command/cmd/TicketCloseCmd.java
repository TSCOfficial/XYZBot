package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.embed.TicketCloseRequestEmbed;
import ch.frily.xyzbot.embed.TicketClosedOptionsEmbed;
import ch.frily.xyzbot.feature.Ticket;
import ch.frily.xyzbot.feature.TicketController;
import ch.frily.xyzbot.interaction.button.btn.TicketArchiveBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketCloseRequestAcceptBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketCloseRequestRejectBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketDeleteBtn;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import javassist.NotFoundException;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class TicketCloseCmd implements ISlashSubcommand {
    @Override
    public String getName() {
        return "close";
    }

    @Override
    public String getDescription() {
        return "Erstelle eine Schliessanfrage";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.BOOLEAN, "erzwingen", "Ticketschliessung erzwingen")
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        try {
            Ticket ticket = TicketController.getTicketById(event.getChannelIdLong());

            if (event.getOption("erzwingen") != null && event.getOption("erzwingen").getAsBoolean()) {
                ActionRow actionRow = ActionRow.of(
                        new TicketArchiveBtn().build(),
                        new TicketDeleteBtn().build()
                );
                ticket.close();

                TicketClosedOptionsEmbed optionsEmbed = new TicketClosedOptionsEmbed();
                optionsEmbed.setMember(ticket.getAssignee());
                optionsEmbed.setChannel(ticket.getChannel());
                event.getHook().sendMessageEmbeds(optionsEmbed.build()).setComponents(actionRow).queue();

            } else {
                ActionRow actionRow = ActionRow.of(
                        new TicketCloseRequestAcceptBtn().build(),
                        new TicketCloseRequestRejectBtn().build()
                );

                TicketCloseRequestEmbed requestEmbed = new TicketCloseRequestEmbed();
                requestEmbed.setMember(ticket.getAssignee());
                requestEmbed.setChannel(ticket.getChannel());
                event.getHook().sendMessageEmbeds(requestEmbed.build()).setComponents(actionRow).queue();
            }


        } catch (SQLException | NotFoundException exception) {
            event.getHook().editOriginal(exception.getMessage()).queue();
        }
    }
}
