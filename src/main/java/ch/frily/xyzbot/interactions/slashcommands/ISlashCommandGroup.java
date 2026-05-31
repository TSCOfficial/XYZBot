package ch.frily.xyzbot.interactions.slashcommands;

import net.dv8tion.jda.api.Permission;

import java.util.List;

public interface ISlashCommandGroup {

    String getName();

    default List<Permission> getDefaultPermissions() {
        return List.of();
    }

    //Map<IPermissionHolder, List<Permission>> getOverwritePermissions();

    List<ISlashSubcommand> getSubcommands();
}
