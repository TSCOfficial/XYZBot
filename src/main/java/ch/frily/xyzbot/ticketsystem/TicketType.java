package ch.frily.xyzbot.ticketsystem;

import lombok.Getter;

public enum TicketType {
    SUPPORT("sup", "Support"),
    BEWERBUNG_ENTWICKLUNG("dev", "Bewerbung Bereich Entwicklung"),
    BEWERBUNG_BAUTRUPP("bau", "Bewerbung Bereich "),
    BEWERBUNG_GESTALTUNG("design", "Bewerbung Bereich Gestaltung"),
    BEWERBUNG_SUPPORT("sup", "Bewerbung Bereich Support"),
    LEITUNG("leit", "Leitung");

    @Getter
    private final String id;
    @Getter
    private final String label;

    private TicketType(String id, String label){
        this.id = id;
        this.label = label;
    }
}
