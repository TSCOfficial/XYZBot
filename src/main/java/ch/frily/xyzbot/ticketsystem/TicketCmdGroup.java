package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.interactions.slashcommands.ISlashCommandGroup;
import ch.frily.xyzbot.interactions.slashcommands.ISlashSubcommand;

import java.util.List;

public class TicketCmdGroup implements ISlashCommandGroup {
    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public List<ISlashSubcommand> getSubcommands() {
        return List.of(
                new PanelSendCmd()
        );
    }
}
