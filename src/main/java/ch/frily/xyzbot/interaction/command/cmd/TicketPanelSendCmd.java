package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import ch.frily.xyzbot.interaction.button.btn.TicketPanelBewerbungButton;
import ch.frily.xyzbot.interaction.button.btn.TicketPanelLeitungBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketPanelSupportBtn;
import ch.frily.xyzbot.embed.TicketPanelEmbed;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import javassist.NotFoundException;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            MessageEmbed embed = new TicketPanelEmbed().build();
            TextChannel channel = EnvResolver.getChannelById(TextChannel.class, EnvKey.GUILD_XYZCRAFT, EnvKey.CHANNEL_TICKET);

            if (channel == null) {
                throw new NotFoundException("Channel not found");
            }
            ActionRow actionrow = ActionRow.of(
                    new TicketPanelSupportBtn().build(),
                    new TicketPanelBewerbungButton().build(),
                    new TicketPanelLeitungBtn().build()
            );
            channel.sendMessageEmbeds(embed).setComponents(actionrow).queue();
            event.reply("✅ Panel erfolgreich gesendet.").setEphemeral(true).queue();

        } catch (NotFoundException notFoundException) {
            event.reply(notFoundException.getMessage()).setEphemeral(true).queue();
        }
    }
}
