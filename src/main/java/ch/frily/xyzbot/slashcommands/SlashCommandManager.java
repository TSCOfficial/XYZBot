package ch.frily.xyzbot.slashcommands;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.find.FindCmd;
import ch.frily.xyzbot.teamlist.TeamlistCmd;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SlashCommandManager {

    private static SlashCommandManager instance;

    @Getter
    private List<ISlashCommand> slashCommands = new ArrayList<>();

    /**
     * Singleton - get instance
     * @return
     */
    public static SlashCommandManager getInstance() {
        if (instance == null) {
            instance = new SlashCommandManager();
        }
        return instance;
    }

    /**
     * Load all slash single commands and command-groups
     */
    public void loadCommands() {
        slashCommands.addAll(List.of(
                new TeamlistCmd(),
                new FindCmd()
        ));
        updateDiscordCommands();
    }

    public void updateDiscordCommands() {

        // Löscht ALLE globalen Commands auf einmal
        Client.getInstance().getClient().updateCommands().queue(
                success -> log.info("Alle Commands gelöscht"),
                error   -> log.error("Fehler: ", error)
        );

//        Client.getInstance().getClient().retrieveCommands().submit().thenAccept(commands -> {
//            commands.forEach((cmd) -> {
//                log.warn("Deleting: " + cmd.getName() + " - " + cmd.getDescription());
//                Client.getInstance().getClient().deleteCommandById(cmd.getId()).queue();
//            });
//        }).exceptionally(exception -> {
//            log.error(exception.getMessage());
//            return null;
//        });

        for (ISlashCommand command : slashCommands) {

            SlashCommandData slashCommand = Commands.slash(command.getName(), command.getDescription());

            if (command instanceof ISlashCommandGroup) {

            }


            if (!command.getOptions().isEmpty()) {
                slashCommand.addOptions(command.getOptions());
            }
            if (!command.getDefaultPermissions().isEmpty()) {
                slashCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getDefaultPermissions()));
            }

            EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT).upsertCommand(slashCommand).queue();
            log.info("Slashcommand: " + command.getName());
        }
    }

    /**
     * Add a slashcommand
     * @param command SlashCommand object
     */
    public void add(ISlashCommand command) {
        slashCommands.add(command);
    }
}
