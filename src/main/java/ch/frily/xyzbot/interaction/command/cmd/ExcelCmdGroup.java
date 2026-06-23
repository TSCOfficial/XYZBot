package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.interaction.command.ISlashCommandGroup;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;

import java.util.List;

public class ExcelCmdGroup implements ISlashCommandGroup {
    @Override
    public String getName() {
        return "excel";
    }

    @Override
    public List<ISlashSubcommand> getSubcommands() {
        return List.of(
                new ExcelImportCmd(),
                new ExcelExportCmd()
        );
    }
}
