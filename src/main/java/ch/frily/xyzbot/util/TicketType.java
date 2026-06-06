package ch.frily.xyzbot.util;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum TicketType {
    SUPPORT_GENERAL(
            "support",
            "📩 Allgemeiner Support",
            "Fragen, Probleme oder sonstige Anliegen – wir helfen dir weiter.",
            """
                    Bitte beschreibe dein Anliegen so genau wie möglich:
 
                    - Worum geht es? (Kurze Zusammenfassung)
                    - Was hast du bereits versucht, um das Problem zu lösen?
                    - Seit wann besteht das Problem?
 
                    Das Supportteam meldet sich so schnell wie möglich bei dir.
                    """,
            TicketTypeGroup.SUPPORT,
            List.of(EnvKey.ROLE_SUPPORT)
    ),
    SUPPORT_ENTBANNANTRAG(
            "unban",
            "🔓 Entbannungs- / Entmutungsantrag",
            "Du wurdest gebannt oder gemutet und möchtest deine Unschuld zeigen.",
            """
                    Bitte beantworte folgende Fragen wahrheitsgemäss und vollständig:
     
                    1. **Dein Minecraft-Name**
                    2. **Art der Strafe** – Ban / Mute?
                    3. **Wann wurdest du bestraft?** – Ungefähres Datum und Uhrzeit
                    4. **Genannter Grund** – Was wurde dir als Grund mitgeteilt?
                    5. **Deine Erklärung** – Warum sollte die Strafe aufgehoben oder reduziert werden?
                    6. **Beweise** – Falls vorhanden, lade sie hier hoch.
     
                    -# Wir bitten dich erhlich zu sein. Unehrliche Anträge werden abgelehnt und können zu verlängerten Strafen führen.
                    """,
            TicketTypeGroup.SUPPORT,
            List.of(EnvKey.ROLE_MODERATION, EnvKey.ROLE_SUPPORT)
    ),
    SUPPORT_BUGMELDUNG(
            "bug",
            "🐛 Fehler / Bug melden",
            "Du hast einen Bug, Glitch oder unerwartetes Verhalten entdeckt.",
            """
                    Bitte beantworte folgende Punkte so genau wie möglich:
 
                    1. **Was ist der Fehler?** – Was ist passiert?
                    2. **Wann ist es passiert?** – Ungefähre Uhrzeit / Datum
                    3. **Wie ist es passiert?** – Was hast du gemacht, kurz bevor der Fehler aufgetreten ist?
                    4. **Reproduzierbar?** – Kannst du den Fehler wiederholen? Falls ja, welche Schritte?
                    5. **Weitere beteiligte Spieler?** – Waren andere Personen dabei oder daran beteiligt?
                    6. **Screenshots / Videos?** – Lade Anhänge gerne direkt mit hoch.
 
                    Je detaillierter deine Beschreibung, desto schneller können wir den Fehler beheben!
                    """,
            TicketTypeGroup.SUPPORT,
            List.of(EnvKey.ROLE_ENTWICKLUNG)
    ),
    SUPPORT_PLAYER_REPORT(
            "report",
            "🚨 Spieler melden",
            "Melde einen Spieler wegen Regelverstoß.",
            """
                    Bitte fülle folgende Angaben wahrheitsgemäss aus:
 
                    1. **Minecraft-Name des Spielers** – Wen möchtest du melden?
                    2. **Grund der Meldung** – Was hat der Spieler getan?
                    3. **Wann ist es passiert?** – Ungefähre Uhrzeit / Datum
                    4. **Beweise** – Screenshots oder Videos bitte hier hochladen.
                       Ohne Beweise können wir leider nur eingeschränkt handeln.
 
                    -# Falsche Meldungen sind nichtig und können bestraft werden!
                    """,
            TicketTypeGroup.SUPPORT,
            List.of(EnvKey.ROLE_SUPPORT)
    ),
    BEWERBUNG_ENTWICKLUNG(
            "dev",
            "💻 Entwicklung",
            "Bewirb dich für den Bereich Entwicklung und unterstütze unser technisches Team.",
            """
                    Vielen Dank für dein Interesse!
                    
                    Damit wir deine Bewerbung beachten könne, beantworte bitte folgende Fragen:
 
                    1. **Name** – Bürgerlicher Name oder Alias - du kannst entscheiden.
                    2. **Minecraft-Spielername**
                    3. **Alter**
                    4. **Erfahrungen** – Welche Programmiersprachen beherrschst du? Wie lange entwickelst du schon? Was hast du bisher gemacht?
                    5. **Motivation** – Warum möchtest du Teil unseres Teams werden?
                    6. **Verfügbarkeit** – Wie viele Stunden pro Woche stehst du zur Verfügung? Zu welchen Zeiten?
                    7. **Projekte** – Teile uns **mindestens zwei Projekte** (GitHub-Links, Videos, etc.) mit, die wir im Rahmen der Bewerbung anschauen können.
 
                    Erzähl uns gerne mehr über dich!
                    """,
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),

    BEWERBUNG_BAUTRUPP(
            "bau",
            "⚒️ Bautrupp",
            "Bewirb dich für den Bereich Bautrupp und gestalte unsere Spielwelt mit.",
            """
                    Vielen Dank für dein Interesse!
                    
                    Damit wir deine Bewerbung beachten könne, beantworte bitte folgende Fragen:
 
                    1. **Name** – Bürgerlicher Name oder Alias - du kannst entscheiden.
                    2. **Minecraft-Spielername**
                    3. **Alter**
                    4. **Baustil & Erfahrung** – Welche Stile beherrschst du? (z. B. Medieval, Modern, Fantasy, ...) Wie lange baust du schon?
                    5. **Motivation** – Warum möchtest du Teil unseres Bautrupps werden?
                    6. **Verfügbarkeit** – Wie viele Stunden pro Woche stehst du zur Verfügung? Zu welchen Zeiten?
                    7. **Portfolio** – Zeige uns **mindestens zwei Bauwerke, Maps oder Projekte**, die wir für die Bewerbung anschauen können. (Screenshots, Videos, etc.)
 
                    Erzähl uns gerne mehr über dich!
                    """,
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),
    BEWERBUNG_GESTALTUNG(
            "design",
            "🎨 Gestaltung",
            "Bewirb dich für den Bereich Gestaltung und präge das visuelle Erscheinungsbild des Servers.",
            """
                   Vielen Dank für dein Interesse!
                    
                   Damit wir deine Bewerbung beachten könne, beantworte bitte folgende Fragen:
 
 
                    1. **Name** – Bürgerlicher Name oder Alias - du kannst entscheiden.
                    2. **Minecraft-Spielername**
                    3. **Alter**
                    4. **Stil & Erfahrung** – Welche Design-Bereiche deckst du ab? (z. B. Logo, Banner, UI, Social Media, ...) Welche Programme verwendest du?
                    5. **Motivation** – Warum möchtest du Teil unseres Gestaltungsteams werden?
                    6. **Verfügbarkeit** – Wie viele Stunden pro Woche stehst du zur Verfügung? Zu welchen Zeiten?
                    7. **Portfolio** – Teile uns **mindestens zwei Grafiken oder Projekte** mit, die wir für die Bewerbung anschauen können.
 
                    Erzähl uns gerne mehr über dich!
                    """,
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),
    BEWERBUNG_SUPPORT(
            "supvote",
            "🔰 Support",
            "Bewirb dich für den Bereich Support. (Teil vom Support-Vote: Freischaltung benötigt!)",
            """
                   Vielen Dank für dein Interesse!
                    
                   Damit wir deine Bewerbung beachten könne, beantworte bitte folgende Fragen:
 
                    1. **Name** – Bürgerlicher Name oder Alias - du kannst entscheiden.
                    2. **Minecraft-Spielername**
                    3. **Alter**
                    4. **Erfahrung** – Hast du bereits Erfahrung im Bereich Support oder in ähnlichen Rollen? Falls ja, wo und wie lange?
                    5. **Motivation** – Warum möchtest du Teil unseres Support-Teams werden? Was zeichnet dich aus?
                    6. **Stärken & Schwächen** – Wie gehst du mit Konflikten um? Wie reagierst du unter Stress?
                    7. **Verfügbarkeit** – Wie viele Stunden pro Woche stehst du zur Verfügung? Zu welchen Zeiten?
 
                    Erzähl uns gerne mehr über dich!
                    """,
            TicketTypeGroup.BEWERBUNG,
            List.of()
    ),
    BEWERBUNG_SOCIALMEDIA(
            "social",
            "🎬 Social Media",
            "Bewirb dich für das Social Media Team und präsentiere den Server deiner Community.",
            """
                    Vielen Dank für dein Interesse!
                    
                    Bitte beantworte folgende Fragen in deiner Bewerbung:
     
                    1. **Name** – Bürgerlicher Name oder Alias - du kannst entscheiden.
                    2. **Minecraft-Spielername**
                    3. **Alter**
                    4. **Plattformen** – YouTube, Twitch, TikTok, ...?
                    5. **Reichweite** – Wie viele Abonnenten / Follower hast du? Wie hoch sind deine durchschnittlichen Views / Zuschauer?
                    6. **Content-Art** – Welchen Content erstellst du? (Let's Play, Tutorials, Highlights, ...?)
                    7. **Links** – Bitte teile Links zu deinen Kanälen und **mindestens einem aktuellen Video / Stream**.
                    8. **Motivation** – Warum möchtest du dem Social Media Team beitreten?
     
                    Wir freuen uns auf deine Bewerbung! 🎥
                    """,
            TicketTypeGroup.BEWERBUNG,
            List.of(EnvKey.ROLE_HR_SOCIALMEDIA)
    ),
    BEWERBUNG_EVENTTEAM(
            "event",
            "🎉 Eventteam",
            "Bewirb dich im Eventteam und organisiere unvergessliche Community-Events.",
            """
                    Vielen Dank für dein Interesse!
                    
                    Bitte beantworte folgende Fragen in deiner Bewerbung:
     
                    1. **Name** – Bürgerlicher Name oder Alias - du kannst entscheiden.
                    2. **Minecraft-Spielername**
                    3. **Alter**
                    4. **Erfahrung** – Hast du bereits Events organisiert? Falls ja, welche?
                    5. **Event-Ideen** – Welche Events würdest du gerne auf dem Server organisieren?
                    6. **Motivation** – Warum möchtest du Teil des Eventteams werden?
                    7. **Verfügbarkeit** – Zu welchen Zeiten bist du verfügbar?
     
                    Erzähl uns gerne mehr über dich!
                    """,
            TicketTypeGroup.BEWERBUNG,
            List.of(EnvKey.ROLE_HR_LEITUNG)
    ),
    LEITUNG_GENERAL(
            "leitung",
            "👑 Leitung kontaktieren",
            "Für Anliegen, die der Support nicht lösen kann, oder vertrauliche Themen.",
            """
                    *Dieses Ticket erreicht direkt die Serverleitung.*
 
                    Bitte beschreibe dein Anliegen so detailliert wie möglich:
                    
                    - Was ist das Problem / dein Wunsch?
                    - Was wurde bisher unternommen?
                    - Gibt es Beweise oder relevante Informationen? (Bitte lade sie hier hoch)
                    """,
            TicketTypeGroup.LEITUNG,
            List.of()
    ),
    LEITUNG_TEAMMELDEN(
            "teamrep",
            "⚠️ Teammitglied melden",
            "Melde ein Teammitglied wegen Fehlverhaltens oder Regelverstosses.",
            """
                    *Dieses Ticket wird vertraulich behandelt und direkt von der Leitung bearbeitet.*
 
                    Bitte fülle folgende Punkte wahrheitsgemäss aus:
 
                    1. **Name des Teammitglieds** – Wen möchtest du melden?
                    2. **Grund der Meldung** – Was hat das Teammitglied getan?
                    3. **Wann ist es passiert?** – Ungefähre Uhrzeit / Datum
                    4. **Beweise** – Gemäss Regelwerk Punkt 16. sind für bestimmte Anschuldigungen Beweise erforderlich. Bitte lade Screenshots, Videos oder Ähnliches direkt hier hoch.
 
                    -# Deine Meldung wird ernst genommen. Falsche Meldungen sind jedoch nichtig und können bestraft werden!
                    """,
            TicketTypeGroup.LEITUNG,
            List.of()
    );

    @Getter
    private final String id;
    @Getter
    private final String label;
    @Getter
    private final String selectDescription;
    @Getter
    private final String embedDescription;
    @Getter
    private final TicketTypeGroup group;
    @Getter
    private final List<Role> responsibleRoles;

    TicketType(String id, String label, String selectDescription, String embedDescription, TicketTypeGroup group, List<EnvKey> responsibleRoles){
        this.id = id;
        this.label = label;
        this.selectDescription = selectDescription;
        this.embedDescription = embedDescription;
        this.group = group;
        this.responsibleRoles = responsibleRoles.stream().map(EnvResolver::getRoleById).toList();
    }

    /**
     * Checks the enum for duplicate entries : Used due to its multiple use-cases in connection with the database etc.
     */
    static {
        Set<String> seen = new HashSet<>();
        for (TicketType type : values()) {
            if (!seen.add(type.getId())) {
                throw new ExceptionInInitializerError(
                        "Duplicate TicketType ID: \"" + type.getId() + "\""
                );
            }
        }
    }
}
