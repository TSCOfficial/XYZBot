package ch.frily.xyzbot.find;

import ch.frily.xyzbot.slashcommands.ISlashCommand;
import javassist.NotFoundException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class FindCmd implements ISlashCommand {
    @Override
    public String getName() {
        return "find";
    }

    @Override
    public String getDescription() {
        return "Finde den Minecraft-Namen einer Person.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.USER, "person", "Discord User").setRequired(true));
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
        event.deferReply(true).queue();

        OptionMapping personOption = event.getInteraction().getOption("person");
        StringBuilder response = new StringBuilder();

        try {
            String playername = Find.getInstance().findPlayername(personOption.getAsUser().getIdLong());
            response.append(personOption.getAsUser().getAsMention());
            response.append("'s Minecraft-Name lautet = ");
            response.append(playername);

        } catch (NotFoundException notFoundException) {
            response.append(personOption.getAsUser().getAsMention());
            response.append("'s Minecraft-Name konnte nicht gefunden werden. Die Person hat ihr Minecraft-Konto nicht mit Discord verknüpft.");
        }

        event.getHook().sendMessage(response.toString()).queue();

    }
}
