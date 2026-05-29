package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.slashcommands.ISlashCommandGroup;
import ch.frily.xyzbot.slashcommands.ISlashSubcommand;
import net.dv8tion.jda.api.Permission;

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
