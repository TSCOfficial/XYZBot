package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.interaction.command.ISlashCommandGroup;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;

import java.util.List;

public class TicketCmdGroup implements ISlashCommandGroup {
    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public List<ISlashSubcommand> getSubcommands() {
        return List.of(
                new TicketPanelSendCmd(),
                new TicketCloseCmd()
        );
    }
}
