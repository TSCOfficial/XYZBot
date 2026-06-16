package ch.frily.xyzbot.feature;

import javassist.NotFoundException;

public class Find {

    public static Find instance;

    public static Find getInstance(){
        if (instance == null) {
            instance = new Find();
        }
        return instance;
    }

    /**
     *
     * @param discordUsernameId
     * @return
     * @throws NotFoundException When the given discord user did not connect their Minecraft Account to Discord.
     */
    public String findPlayername(long discordUsernameId) throws NotFoundException {
        // todo retrieve playername by matching the discord username ID with the saved account-link data from the DB
        String playername = "";

        if (!playername.equals("") && playername != null) {
            // todo handle existing links
        }
        throw new NotFoundException("Diese Person hat sein Minecraft Konto nicht mit dem Discord verbunden.");
    }
}
