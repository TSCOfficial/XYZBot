package ch.frily.xyzbot.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Table {
    TICKET("ticket", TicketColumn.class),
    USER("user", UserColumn.class);

    private final String table;
    private final Class<? extends Column> columnClass;

    /**
     * Common interface
     */
    public interface Column {
        String getColumn();
    }

    // TICKET
    @Getter
    @RequiredArgsConstructor
    public enum TicketColumn implements Column {
        ID("id"),
        TITLE("title"),
        STATUS("status"),
        CREATED_AT("created_at");

        private final String column;
    }

    // USER
    @Getter
    @RequiredArgsConstructor
    public enum UserColumn implements Column {
        ID("id"),
        USERNAME("username"),
        EMAIL("email");

        private final String column;
    }
}