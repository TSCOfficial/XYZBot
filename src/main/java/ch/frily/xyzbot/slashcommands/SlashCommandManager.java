package ch.frily.xyzbot.slashcommands;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.utils.IdResolver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * Load all slash commands
     */
    public void loadCommands() {
        Reflections reflections = new Reflections("ch.frily.xyzbot.slashcommands.cmds");

        Set<Class<? extends ISlashCommand>> commandClasses = reflections.getSubTypesOf(ISlashCommand.class);

        for (Class<? extends ISlashCommand> commandClass : commandClasses) {
            try {
                slashCommands.add(commandClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            SlashCommandData slashCommandData = Commands.slash(command.getName(), command.getDescription());
            if (!command.getOptions().isEmpty()) {
                slashCommandData.addOptions(command.getOptions());
            }
            if (!command.getDefaultPermissions().isEmpty()) {
                slashCommandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getDefaultPermissions()));
            }

            IdResolver.getGuildById("GUILD_XYZCRAFT").upsertCommand(slashCommandData).queue();
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
