package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.slashcommands.ISlashCommand;
import ch.frily.xyzbot.slashcommands.ISlashCommandGroup;
import ch.frily.xyzbot.slashcommands.ISlashSubcommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PanelSendCmd implements ISlashSubcommand {
    @Override
    public ISlashCommandGroup getGroup() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public Map<String, List<?>> getAutocomplete() {
        return Map.of();
    }

    @Override
    public List<Permission> getDefaultPermissions() {
        return List.of();
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

    }
}
