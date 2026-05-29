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
            "Probleme die der Support ned lösen kann");

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
