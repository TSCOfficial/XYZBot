package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.Client;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import ch.frily.xyzbot.utils.Field;
import ch.frily.xyzbot.utils.IEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PanelEmbed implements IEmbed {
    @Override
    public String getAuthorName() {
        return "XYZCraft";
    }

    @Override
    public String getAuthorIconUrl() {
        return EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT).getIconUrl();
    }

    @Override
    public String getDescription() {
        return "Wähle für dein Anliegen die passende Kategorie aus und folge den darauffolgenden Anweisungen.";
    }

    @Override
    public List<Field> getFields() {
        return Arrays.stream(TicketTypeGroup.values()).map(type -> new Field(type.getLabel(), type.getDescription(), false)).toList();
    }

    @Override
    public String getFooterText() {
        return "XYZCraft Support";
    }

    @Override
    public Instant getTimestamp() {
        return null;
    }
}
