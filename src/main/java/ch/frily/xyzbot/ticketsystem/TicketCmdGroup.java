package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.interactions.slashcommand.ISlashCommandGroup;
import ch.frily.xyzbot.interactions.slashcommand.ISlashSubcommand;
import ch.frily.xyzbot.ticketsystem.panel.PanelSendCmd;
import ch.frily.xyzbot.ticketsystem.panel.interaction.TicketCloseCmd;

import java.util.List;

public class TicketCmdGroup implements ISlashCommandGroup {
    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public List<ISlashSubcommand> getSubcommands() {
        return List.of(
                new PanelSendCmd(),
                new TicketCloseCmd()
        );
    }
}
