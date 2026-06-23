package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.feature.ExcelControl;
import ch.frily.xyzbot.interaction.command.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ExcelCmd implements ISlashCommand {
    @Override
    public String getName() {
        return "excel";
    }

    @Override
    public String getDescription() {
        return "Download Excel-Datei";
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            FileUpload file = ExcelControl.getInstance().generateExcel();
            event.getChannel().sendFiles(file).queue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
