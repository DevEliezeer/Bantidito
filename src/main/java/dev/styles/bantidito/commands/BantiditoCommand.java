package dev.styles.bantidito.commands;

import dev.styles.bantidito.utilities.ColorUtil;
import dev.styles.bantidito.utilities.SenderUtil;
import dev.styles.bantidito.utilities.command.BaseCommand;
import dev.styles.bantidito.utilities.command.Command;
import dev.styles.bantidito.utilities.command.CommandArgs;
import dev.styles.bantidito.Bantidito;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class BantiditoCommand extends BaseCommand {

    private final Bantidito plugin;

    @Override
    @Command(name = "bantidito")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length != 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("bantidito.reload")) {
                SenderUtil.sendMessage(sender, "&cYou dont have permissions to execute this command.");
                return;
            }

            try {
                plugin.onReload();
            } catch (Exception e) {
                e.printStackTrace();
                SenderUtil.sendMessage(sender, "&cAn error occurred while reloading the configuration files.");
                return;
            }

            SenderUtil.sendMessage(sender, "&aAll config files were successfully reloaded.");
            return;
        }

        sender.sendMessage(ColorUtil.translate(new String[]{
                "",
                "     &3&lBantidito &7- " + plugin.getDescription().getVersion(),
                "",
                " &7● &fVersion&7: &3" + plugin.getDescription().getVersion(),
                " &7● &fAuthor&7: &3" + plugin.getDescription().getAuthors(),
                "",
                " &7&oStyles Development @ styles.dev",
                ""
        }));
    }
}
