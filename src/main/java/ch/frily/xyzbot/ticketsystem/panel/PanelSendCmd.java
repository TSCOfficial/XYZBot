package ch.frily.xyzbot.ticketsystem.panel;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.interactions.slashcommand.ISlashSubcommand;
import ch.frily.xyzbot.ticketsystem.panel.interaction.BewerbungButton;
import ch.frily.xyzbot.ticketsystem.panel.interaction.LeitungButton;
import ch.frily.xyzbot.ticketsystem.panel.interaction.SupportButton;
import ch.frily.xyzbot.utils.EnvKey;
import javassist.NotFoundException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PanelSendCmd implements ISlashSubcommand {

    @Override
    public String getName() {
        return "panel";
    }

    @Override
    public String getDescription() {
        return "Sende panel";
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
        try {
            MessageEmbed embed = new PanelEmbed().build();
            TextChannel channel = event.getGuild().getTextChannelById(Client.getInstance().getConfig().get(EnvKey.CHANNEL_TICKET.name()));

            if (channel == null) {
                throw new NotFoundException("Channel not found");
            }
            ActionRow actionrow = ActionRow.of(
                    SupportButton.getInstance().build(),
                    BewerbungButton.getInstance().build(),
                    LeitungButton.getInstance().build()
            );
            channel.sendMessageEmbeds(embed).setComponents(actionrow).queue();
            event.reply("✅ Panel erfolgreich gesendet.").setEphemeral(true).queue();

        } catch (NotFoundException notFoundException) {
            event.reply(notFoundException.getMessage()).setEphemeral(true).queue();
        }
    }
}
