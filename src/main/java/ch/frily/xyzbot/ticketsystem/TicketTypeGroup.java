package ch.frily.xyzbot.ticketsystem;

import lombok.Getter;

/**
 * Group of tickettypes<br>
 * This is used for the Ticket Panel
 */
public enum TicketTypeGroup {
    SUPPORT(
            "Support",
            "Wenn du ein Anliegen hast, ob Problem oder sonstiges."
    ),
    BEWERBUNG(
            "Bewerbung Bereich Entwicklung",
            "Bewerbung als Dev"),
    LEITUNG(
            "Leitung",
            """
                    Bitte nutze dieses Ticket nur, wenn:
                    - Dein Anliegen über die Möglichkeiten des Supports hinausgeht.
                    - Es sich um ein vertrauliches oder sensibles Thema handelt.
                    - Du bereits ein Support-Ticket hattest, das nicht gelöst wurde.
                    """);

    @Getter
    private final String label;
    @Getter
    private final String description;

    /**
     *
     * @param label Label of the group (displayed on the panel)
     * @param description Description of the group (displayed on the panel)
     */
    TicketTypeGroup(String label, String description){
        this.label = label;
        this.description = description;
    }
}
