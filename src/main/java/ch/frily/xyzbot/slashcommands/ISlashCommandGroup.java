package ch.frily.xyzbot.slashcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Map;

public interface ISlashCommandGroup extends ISlashCommand {

    String getName();

    List<Permission> getDefaultPermissions();

    //Map<IPermissionHolder, List<Permission>> getOverwritePermissions();

    List<ISlashSubcommand> getSubcommands();
}
