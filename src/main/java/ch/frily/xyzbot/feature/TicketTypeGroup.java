package ch.frily.xyzbot.feature;

import lombok.Getter;

/**
 * Group of tickettypes<br>
 * This is used for the Ticket Panel
 */
public enum TicketTypeGroup {
    SUPPORT(
            "Support",
            """
                    Hier findest du Hilfe bei allgemeinen Anliegen, technischen Problemen oder Regelverstössen.
                    """
    ),
    BEWERBUNG(
            "Bewerbung",
            """
                    Möchtest du Teil unseres Teams werden? Hier kannst du dich für verschiedene Bereiche bewerben.
                    """
    ),
    LEITUNG(
            "Leitung",
            """
                    Direkter Kontakt zur Serverleitung für vertrauliche oder komplexe Anliegen.
                    
                    -# Bitte nutze diesen Bereich nur, wenn dein Anliegen nicht durch den Support gelöst werden kann.
                    """
    );

    @Getter
    private final String label;
    @Getter
    private final String description;

    /**
     *
     * @param label Label of the group (displayed on the panel)
     * @param description Description of the group (displayed on the panel)
     */
    TicketTypeGroup(String label, String description) {
        this.label = label;
        this.description = description;
    }
}