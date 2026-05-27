package ch.frily.xyzbot.slashcommands.cmds;

import ch.frily.xyzbot.slashcommands.ISlashCommand;
import ch.frily.xyzbot.teamlist.Teamlist;
import ch.frily.xyzbot.utils.IdResolver;
import net.dv8tion.jda.api.components.Component;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.components.actionrow.ActionRowImpl;
import net.dv8tion.jda.internal.components.buttons.ButtonImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        MessageEmbed embed = Teamlist.getInstance().generateEmbed();
        TextChannel channel = (TextChannel) IdResolver.getChannelById("GUILD_XYZCRAFT", "CHANNEL_TEAMLIST");
        //channel.sendMessage("").addEmbeds(embed).queue();
        ActionRow components = ActionRow.of(Button.link("https://discord.com/channels/719211950269005857/737440736529875035", "Bewerben"));
        event.reply("✅ Teamliste wurde aktualisiert.").setEphemeral(true).setEmbeds(embed).addComponents(components).queue();
    }
}
