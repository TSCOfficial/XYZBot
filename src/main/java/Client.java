import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    final Logger logger = LoggerFactory.getLogger(Client.class);

    @Getter
    private static JDA client;

    @Getter
    private static Dotenv config;

    public static void main(String[] args) {
        try {
            config = loadConfig();
            client = createClient();
            client.awaitReady();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private static JDA createClient() {
        JDABuilder jdaBuilder = JDABuilder.createDefault(config.get("TOKEN"));
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.setStatus(OnlineStatus.IDLE);
        jdaBuilder.setActivity(Activity.customStatus("Lasset die neue Ära beginnen!"));
        return jdaBuilder.build();
    }

    public static Dotenv loadConfig(){
        return Dotenv.configure().load();
    }
}
