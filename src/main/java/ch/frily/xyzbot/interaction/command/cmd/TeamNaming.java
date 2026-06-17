package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.interaction.command.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class TeamNaming implements ISlashCommand {

    @Override
    public String getName() {
        return "teamnaming";
    }

    @Override
    public String getDescription() {
        return "nothing";
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String emojis = ch.frily.xyzbot.feature.TeamNaming.nameMemberByTopRole(event.getMember());
        event.reply(emojis).queue();
    }
}
