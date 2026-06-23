package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.feature.ExcelControl;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelImportCmd implements ISlashSubcommand {
    @Override
    public String getName() {
        return "import";
    }

    @Override
    public String getDescription() {
        return "Importiere eine Excel-Datei.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.ATTACHMENT, "file", "Excel-Datei", true)
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Message.Attachment attachment = event.getOption("file").getAsAttachment();

        attachment.getProxy().download().thenAccept(inputStream -> {
            try (InputStream stream = inputStream) {
                String result = ExcelControl.getInstance().importExcel(stream);
                event.getChannel().sendMessage(result).queue();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}