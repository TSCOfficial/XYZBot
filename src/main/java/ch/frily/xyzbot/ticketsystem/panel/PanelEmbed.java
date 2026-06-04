package ch.frily.xyzbot.ticketsystem.panel;

import ch.frily.xyzbot.ticketsystem.TicketType;
import ch.frily.xyzbot.ticketsystem.TicketTypeGroup;
import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import ch.frily.xyzbot.utils.Field;
import ch.frily.xyzbot.utils.IEmbed;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public String getTitle() {
        return "Ticketsystem";
    }

    @Override
    public String getDescription() {
        return "Wähle den passenden Kontaktbereich aus, drücke den dazugehörigen Button und wähle anschliessend die gewünschte Kategorie aus.";
    }

    @Override
    public List<Field> getFields() {
        return Arrays.stream(TicketTypeGroup.values()).map(typeGroup -> {
            StringBuilder description = new StringBuilder();
            description.append(typeGroup.getDescription());
            description.append("\n**Verfügbare Kategorien:**\n");

            Arrays.stream(TicketType.values()).filter(type -> type.getGroup() == typeGroup).forEach(type -> {
                description.append("- ").append(type.getLabel()).append("\n");
            });


            return new Field(typeGroup.getLabel(), description.toString(), false);
        }).toList();
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
