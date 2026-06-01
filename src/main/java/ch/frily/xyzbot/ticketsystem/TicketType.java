package ch.frily.xyzbot.ticketsystem;

import ch.frily.xyzbot.utils.EnvKey;
import ch.frily.xyzbot.utils.EnvResolver;
import lombok.Getter;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Role;

import java.security.Permission;
import java.util.List;

public enum TicketType {
    SUPPORT_GENERAL(
            "sup",
            "Generelles Anliegen",
            "Wenn du ein Anliegen hast, ob Problem oder sonstiges.",
            TicketTypeGroup.SUPPORT,
            List.of(EnvKey.ROLE_SUPPORT)
    ),
    SUPPORT_BUGMELDUNG(
            "bug",
            "Fehlermeldung",
            "Du hast ein Fehler oder Ähnliches gesehen.",
            TicketTypeGroup.SUPPORT,
            List.of()
    ),
    BEWERBUNG_ENTWICKLUNG(
            "dev",
            "Bewerbung Bereich Entwicklung",
            "Bewerbung als Dev",
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),

    BEWERBUNG_BAUTRUPP(
            "bau",
            "Bewerbung Bereich",
            "Bewerbung als builder",
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),
    BEWERBUNG_GESTALTUNG(
            "design",
            "Bewerbung Bereich Gestaltung",
            "Bewerbung als Designer",
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),
    BEWERBUNG_SUPPORT(
            "sup",
            "Bewerbung Bereich Support",
            "Bewerbung als Sup/Mod",
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),
    LEITUNG_GENERAL(
            "leit",
            "Leitung",
            "Probleme die der Support ned lösen kann",
            TicketTypeGroup.LEITUNG,
            List.of()
    ),
    LEITUNG_TEAMMELDEN(
            "report",
            "Teammelden",
            "Melde ein Teammitglied.",
            TicketTypeGroup.LEITUNG,
            List.of()
    );

    @Getter
    private final String id;
    @Getter
    private final String label;
    @Getter
    private final String description;
    @Getter
    private final TicketTypeGroup group;
    @Getter
    private final List<Role> mentions;

    TicketType(String id, String label, String description, TicketTypeGroup group, List<EnvKey> mentions){
        this.id = id;
        this.label = label;
        this.description = description;
        this.group = group;
        this.mentions = mentions.stream().map(EnvResolver::getRoleById).toList();
    }
}
