package ch.frily.xyzbot.ticketsystem;

import lombok.Getter;

public enum TicketStatus {
    NEW("🔶"),
    CLAIMED("🎫"),
    ARCHIVED("🗃️");

    @Getter
    private final String icon;

    TicketStatus(String icon){
        this.icon = icon;
    }
}
