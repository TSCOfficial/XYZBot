package ch.frily.xyzbot.slashcommands;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.utils.IdResolver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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

        List<Command> commands = Client.getInstance().getClient().retrieveCommands().complete();
        commands.forEach((cmd) -> {
            log.warn("Deleting: " + cmd.getName() + " - " + cmd.getDescription());
        });

        for (ISlashCommand command : slashCommands) {
            IdResolver.getGuildById("GUILD_XYZCRAFT").upsertCommand(command.getName(), command.getDescription()).queue();
//            if (command.getOptions() == null) {
//                Client.getInstance().getClient().upsertCommand(
//                        command.getName(),
//                        command.getDescription()).queue();
//            } else {
//                Client.getInstance().getClient().upsertCommand(
//                                command.getName(),
//                                command.getDescription()
//                        )
//                        .addOptions(command.getOptions()).queue();
//            }
            log.info("Slashcommand: " + command.getName());

        }
        //Client.getInstance().getClient().updateCommands().queue();
    }

    /**
     * Add a slashcommand
     * @param command SlashCommand object
     */
    public void add(ISlashCommand command) {
        slashCommands.add(command);
    }
}
