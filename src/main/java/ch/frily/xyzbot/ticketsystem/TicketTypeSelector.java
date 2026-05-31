package ch.frily.xyzbot.ticketsystem;

import net.dv8tion.jda.api.components.selections.SelectMenu;
import net.dv8tion.jda.api.modals.Modal;
import net.dv8tion.jda.internal.components.selections.SelectMenuImpl;
import net.dv8tion.jda.internal.modals.ModalImpl;

public class TicketTypeSelector {

    public Modal createModal(){
        Modal.Builder modal = Modal.create("ticket-type-selector", "Tickettyp auswählen");
    }
}
