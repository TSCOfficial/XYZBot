package ch.frily.xyzbot.container;

import ch.frily.xyzbot.feature.TicketManager;
import ch.frily.xyzbot.feature.TicketType;
import ch.frily.xyzbot.feature.TicketTypeGroup;
import ch.frily.xyzbot.util.Color;
import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.MessageUtil;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TicketPanelContainer extends Container {

    public TicketPanelContainer() {
        this.setColor(new Color("2ecc71").get());

        this.addTextDisplay("## Ticketsystem");
        this.addTextDisplay("Wähle den passenden Kontaktbereich aus, drücke den dazugehörigen Button und wähle anschliessend die gewünschte Kategorie aus.");

        Arrays.stream(TicketTypeGroup.values()).forEach(typeGroup -> {

            String groupCategories = Arrays.stream(TicketType.values()).filter(type -> type.getGroup() == typeGroup).map(type -> {
                return MessageUtil.format("- {}", type.getLabel());
            }).collect(Collectors.joining("\n"));

            this.addSection(
                    TicketManager.getInstance().getButtonByTypeGroup(typeGroup).build(),
                    TextDisplay.of(MessageUtil.format("### {}", typeGroup.getLabel())),
                    TextDisplay.of(typeGroup.getDescription())
            );
            this.addTextDisplay("**Verfügbare Kategorien:**");
            this.addTextDisplay(groupCategories);
            this.addLineSeparator(Separator.Spacing.LARGE);
        });
        this.addTextDisplay("-# Falls du ein generelles Anliegen hast oder dein Anliegen nicht zwingend Privat geklärt werden muss, kannst du auch <#1088924992240091227> verwenden.");
    }
}
