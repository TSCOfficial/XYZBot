package ch.frily.xyzbot.slashcommands;

import ch.frily.xyzbot.find.FindCmd;
import ch.frily.xyzbot.teamlist.TeamlistCmd;
import ch.frily.xyzbot.ticketsystem.PanelSendCmd;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SlashCommandRegistry {

    private final Map<String, ISlashCommand> commands = new HashMap<>();
    private final List<ISlashCommandGroup> groups = new ArrayList<>();
    private final Map<String, ISlashSubcommand> subcommands = new HashMap<>();

    public void loadCommands() {
        List<ISlashCommand> slashCommands = List.of(
                new FindCmd(),
                new TeamlistCmd(),
                new PanelSendCmd()
        );

        List<ISlashCommandGroup> slashCommandGroups = List.of(

        );

        slashCommands.forEach(cmd -> {
            commands.put(cmd.getName(), cmd);
        });

        slashCommandGroups.forEach(group -> {
            groups.add(group);
            group.getSubcommands().forEach(cmd -> {
                subcommands.put(cmd.getGroup().getName() + " " + cmd.getName(), cmd);
            });

        });
    }

    public void registerAll() {
        Guild guild = EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT);
        List<CommandData> commandDataList = new ArrayList<>();

        commands.forEach((name, cmd) -> {
            commandDataList.add(buildCommand(cmd));
        });

        groups.forEach(group -> {
            commandDataList.add(buildGroup(group));
        });

        guild.updateCommands().addCommands(commandDataList).queue(
                s -> log.info("Alle Commands registriert"),
                e -> log.error("Fehler beim Registrieren: ", e)
        );
    }

    private SlashCommandData buildCommand(ISlashCommand command) {
        SlashCommandData slashCommand = Commands.slash(command.getName(), command.getDescription());
        if (!command.getOptions().isEmpty()) {
            slashCommand.addOptions(command.getOptions());
        }
        if (!command.getDefaultPermissions().isEmpty()) {
            slashCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getDefaultPermissions()));
        }

        return slashCommand;
    }

    private SlashCommandData buildGroup(ISlashCommandGroup group) {
        SlashCommandData SlashCommand = Commands.slash(group.getName(), group.getDescription());
        if (!group.getDefaultPermissions().isEmpty())
            SlashCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(group.getDefaultPermissions()));

        group.getSubcommands().forEach(sub -> {
            SubcommandData subData = new SubcommandData(sub.getName(), sub.getDescription());
            if (!sub.getOptions().isEmpty()) subData.addOptions(sub.getOptions());
            SlashCommand.addSubcommands(subData);
        });

        return SlashCommand;
    }

    /**
     * Dispatch the event from an eventlistener to the appropriate interaction executor
     * @param event
     */
    public void dispatchInteractionEvent(SlashCommandInteractionEvent event) {
        String subName = event.getSubcommandName();

        if (subName != null) {
            // Subcommand: "group/subcommand"
            ISlashSubcommand command = subcommands.get(event.getName() + " " + subName);
            if (command != null) command.execute(event);
        } else {
            ISlashCommand command = commands.get(event.getName());
            if (command != null) command.execute(event);
        }
    }
}
