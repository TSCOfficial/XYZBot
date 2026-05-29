package ch.frily.xyzbot.ticketsystem;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Arrays;

public class PanelManager {

    private static PanelManager instance;

    public PanelManager getInstance() {
        if (instance == null) {
            instance = new PanelManager();
        }
        return instance;
    }

    public MessageEmbed createEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Ticketsystem");

        Arrays.stream(TicketType.values()).map(type -> {
            return embed.addField(type.getLabel(), type.getDescription(), false);
        });
        return embed.build();
    }
}
