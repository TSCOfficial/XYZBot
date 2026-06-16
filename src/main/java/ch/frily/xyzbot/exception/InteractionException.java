package ch.frily.xyzbot.exception;

import lombok.Getter;
import org.slf4j.event.Level;

/**
 * Exception that can be reported back to the user during a Discord interaction.
 * <p>
 * In addition to a user-facing message, an optional {@code hint} can be provided.
 * The hint is rendered small (using Discord's {@code -#} subtext markdown) below
 * the error message and is meant to explain <i>why</i> the error happened
 * (e.g. missing permissions).
 */
@Getter
public class InteractionException extends ClientException {

    /** Small subtext shown below the error message. May be {@code null}. */
    private final String hint;

    /** Whether the reply to the user should be ephemeral (only visible to them). */
    private final boolean ephemeral;

    public InteractionException(String message) {
        this(message, null);
    }

    public InteractionException(String message, String hint) {
        this(message, hint, true, null, Level.WARN, false);
    }

    public InteractionException(String message, String hint, boolean ephemeral) {
        this(message, hint, ephemeral, null, Level.WARN, false);
    }

    public InteractionException(String message,
                                String hint,
                                boolean ephemeral,
                                Throwable cause,
                                Level logLevel,
                                boolean logStackTrace) {
        super(message, cause, logLevel, logStackTrace);
        this.hint = hint;
        this.ephemeral = ephemeral;
    }

    /**
     * Builds the message that is sent back to the user on Discord.
     * The optional hint is appended as small subtext.
     *
     * @return formatted user response message
     */
    public String toUserMessage() {
        if (hint == null || hint.isBlank()) {
            return getMessage();
        }
        return getMessage() + "\n-# " + hint;
    }
}
