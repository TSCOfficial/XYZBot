package ch.frily.xyzbot.interaction.command.cmd;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.interaction.command.ISlashSubcommand;
import ch.frily.xyzbot.interaction.button.btn.TicketBewerbungButton;
import ch.frily.xyzbot.interaction.button.btn.TicketLeitungBtn;
import ch.frily.xyzbot.interaction.button.btn.TicketSupportBtn;
import ch.frily.xyzbot.embed.TicketPanelEmbed;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            MessageEmbed embed = new TicketPanelEmbed().build();
            TextChannel channel = EnvResolver.getChannelById(TextChannel.class, EnvKey.GUILD_XYZCRAFT, EnvKey.CHANNEL_TICKET);

            if (channel == null) {
                throw new NotFoundException("Channel not found");
            }
            ActionRow actionrow = ActionRow.of(
                    new TicketSupportBtn().build(),
                    new TicketBewerbungButton().build(),
                    new TicketLeitungBtn().build()
            );
            channel.sendMessageEmbeds(embed).setComponents(actionrow).queue();
            event.reply("✅ Panel erfolgreich gesendet.").setEphemeral(true).queue();

        } catch (NotFoundException notFoundException) {
            event.reply(notFoundException.getMessage()).setEphemeral(true).queue();
        }
    }
}
