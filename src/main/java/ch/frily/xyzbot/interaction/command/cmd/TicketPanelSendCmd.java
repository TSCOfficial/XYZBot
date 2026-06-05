package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import ch.frily.xyzbot.interaction.button.btn.TicketBewerbungButton;
import ch.frily.xyzbot.interaction.button.btn.LeitungButton;
import ch.frily.xyzbot.interaction.button.btn.SupportButton;
import ch.frily.xyzbot.embed.TicketPanelEmbed;
import ch.frily.xyzbot.util.EnvKey;
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

public class TicketPanelSendCmd implements ISlashSubcommand {

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
            MessageEmbed embed = new TicketPanelEmbed().build();
            TextChannel channel = event.getGuild().getTextChannelById(Client.getInstance().getConfig().get(EnvKey.CHANNEL_TICKET.name()));

            if (channel == null) {
                throw new NotFoundException("Channel not found");
            }
            ActionRow actionrow = ActionRow.of(
                    SupportButton.getInstance().build(),
                    TicketBewerbungButton.getInstance().build(),
                    LeitungButton.getInstance().build()
            );
            channel.sendMessageEmbeds(embed).setComponents(actionrow).queue();
            event.reply("✅ Panel erfolgreich gesendet.").setEphemeral(true).queue();

        } catch (NotFoundException notFoundException) {
            event.reply(notFoundException.getMessage()).setEphemeral(true).queue();
        }
    }
}
