package ch.frily.xyzbot;

import ch.frily.xyzbot.database.Database;
import ch.frily.xyzbot.interaction.button.ButtonRegistry;
import ch.frily.xyzbot.interaction.modal.ModalRegistry;
import ch.frily.xyzbot.listener.InteractionListener;
import ch.frily.xyzbot.listener.OnMessageReceived;
import ch.frily.xyzbot.listener.OnReadyListener;
import ch.frily.xyzbot.listener.OnGuildMemberUpdate;
import ch.frily.xyzbot.interaction.command.SlashCommandRegistry;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class Client {

    private static Client instance;

    @Getter
    private JDA client;

    @Getter
    private Dotenv config;

    /**
     * Singleton
     * @return Get existing or create instance
     */
    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public static void main(String[] args) {
        getInstance().setup();
    }

    /**
     * Creates and connects every needed thing so that the bot can run normally
     */
    public void setup() {
        try {
            config = loadConfig();
            client = createClient();
            client.awaitReady();
            log.info("Application started successfully!");

            // Load/start stuff

            SlashCommandRegistry.getInstance().loadCommands();
            SlashCommandRegistry.getInstance().registerAll();
            ButtonRegistry.getInstance().loadButtons();
            ModalRegistry.getInstance().loadModals();

        } catch (InterruptedException interruptedException) {
            log.error(interruptedException.getMessage());
        } catch (SQLException sqlException) {
            log.error("SQLState: {}", sqlException.getSQLState());
            log.error(sqlException.getMessage());
        }
    }

    /**
     * Creates the JDA clientUSE
     * @return New JDA client
     */
    private JDA createClient() throws SQLException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(config.get("CRED_TOKEN"));
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.setStatus(OnlineStatus.IDLE);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setActivity(Activity.customStatus("Lasset die neue Ära beginnen!"));

        Connection conn = Database.getInstance().connect();
        if (conn != null) {
            log.info("Database connected!");
        } else {
            throw new SQLException("Database could not be reached!");
        }
        Database.getInstance().disconnect();

        // Event listeners
        jdaBuilder.addEventListeners(InteractionListener.getInstance());
        jdaBuilder.addEventListeners(OnReadyListener.getInstance());
        jdaBuilder.addEventListeners(OnGuildMemberUpdate.getInstance());
        jdaBuilder.addEventListeners(OnMessageReceived.getInstance());
        return jdaBuilder.build();
    }

    /**
     * Load .env-file configurations
     * @return {@link Dotenv} config object
     */
    private Dotenv loadConfig(){
        return Dotenv.configure().load();
    }
}
