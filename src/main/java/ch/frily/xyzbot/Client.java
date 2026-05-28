package ch.frily.xyzbot;

import ch.frily.xyzbot.listeners.InteractionListener;
import ch.frily.xyzbot.listeners.OnReadyListener;
import ch.frily.xyzbot.listeners.RoleUpdateListener;
import ch.frily.xyzbot.slashcommands.SlashCommandManager;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

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
            SlashCommandManager.getInstance().loadCommands();

        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Creates the JDA client
     * @return New JDA client
     */
    private JDA createClient() {
        JDABuilder jdaBuilder = JDABuilder.createDefault(config.get("TOKEN"));
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.setStatus(OnlineStatus.IDLE);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setActivity(Activity.customStatus("Lasset die neue Ära beginnen!"));
        // Event listeners
        jdaBuilder.addEventListeners(InteractionListener.getInstance());
        jdaBuilder.addEventListeners(OnReadyListener.getInstance());
        jdaBuilder.addEventListeners(RoleUpdateListener.getInstance());
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
