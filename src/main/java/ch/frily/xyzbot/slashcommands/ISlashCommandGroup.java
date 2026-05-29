package ch.frily.xyzbot.slashcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Map;

public interface ISlashCommandGroup {

    String getName();

    default List<Permission> getDefaultPermissions() {
        return List.of();
    }

    //Map<IPermissionHolder, List<Permission>> getOverwritePermissions();

    List<ISlashSubcommand> getSubcommands();
}
