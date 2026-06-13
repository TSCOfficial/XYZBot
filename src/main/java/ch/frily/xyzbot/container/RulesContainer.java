package ch.frily.xyzbot.container;

import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.filedisplay.FileDisplay;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

@Slf4j
public class RulesContainer extends Container {

    public RulesContainer(){
        log.debug("RulesContainer");
        this.addComponent(Section.of(
                Thumbnail.fromUrl(EnvResolver.getGuildById(EnvKey.GUILD_XYZCRAFT).getIconUrl()),
                TextDisplay.of("## XYZCraft Regelwerk"),
                TextDisplay.of("Das Regelwerk des XYZCraft-Netzwerks ist online auf unserem DocMost auffindbar.")
        ));
        this.addComponent(Separator.createInvisible(Separator.Spacing.SMALL));

        this.addComponent(Section.of(
                Button.link("https://doc.xyzcraft.de/share/ql35xjvuqg/p/regelwerk-xyz-craft-netzwerk-JvIICndOWE", "Regelwerk"),
                TextDisplay.of("*Von 30.05.2024 — Stand am: 31.05.2026*")
        ));

        this.addComponent(Separator.createDivider(Separator.Spacing.LARGE));
        this.addComponent(TextDisplay.of("-# Beim Verwenden jeglicher Dienste des XYZCraft-Netzwerks stimmst du automatisch dem vorliegenden Regelwerk zu und verpflichtest dich, es einzuhalten. Die Serverleitung behält sich das Recht vor, das Regelwerk ohne Vorankündigung zu ändern. Dieses Regelwerk erhebt keinen Anspruch auf Vollständigkeit."));
    }
}
