package ch.frily.xyzbot.teamlist;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.slashcommands.ISlashCommand;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Slf4j
public class TeamlistCmd implements ISlashCommand {

    @Override
    public String getName() {
        return "teamlist";
    }

    @Override
    public String getDescription() {
        return "Sende oder Aktualisiere die Teamliste.";
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
        return List.of(Permission.ADMINISTRATOR);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        MessageEmbed embed = Teamlist.getInstance().generateEmbed();
        ActionRow components = ActionRow.of(
                Button.link("https://discord.com/channels/719211950269005857/737440736529875035", "Bewerben"),
                Button.link(Client.getInstance().getConfig().get("URL_AUFBAUORGANISATION"), "Aufbauorganisation"));
        TextChannel channel = EnvResolver.getChannelById(TextChannel.class, EnvKey.GUILD_XYZCRAFT, EnvKey.CHANNEL_DASTEAM);

        event.deferReply(true).queue();
        EnvResolver.getMessageById(event.getGuild().getIdLong(), channel.getIdLong(), channel.getLatestMessageIdLong()).thenAccept(message -> {
            message.editMessageEmbeds(embed).queue();
        }).exceptionally(error -> {
            channel.sendMessage("").addEmbeds(embed).addComponents(components).queue();
            return null;
        });
        event.getHook().sendMessage("✅ Teamliste wurde aktualisiert.").setEphemeral(true).queue();



    }
}
