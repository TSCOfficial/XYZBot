package ch.frily.xyzbot.slashcommands;

// Separation of Concerns
public interface ISlashSubcommand extends ISlashCommand {

    ISlashCommandGroup getGroup();
}
