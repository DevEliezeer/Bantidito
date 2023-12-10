package dev.vanitymc.bantidito.commands;

import dev.vanitymc.bantidito.Bantidito;
import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.config.Language;
import dev.vanitymc.bantidito.utilities.SenderUtil;
import dev.vanitymc.bantidito.utilities.command.BaseCommand;
import dev.vanitymc.bantidito.utilities.command.Command;
import dev.vanitymc.bantidito.utilities.command.CommandArgs;
import dev.vanitymc.bantidito.sactions.SanctionsManager;
import dev.vanitymc.bantidito.sactions.menus.sanction.SanctionsMenu;
import org.bukkit.entity.Player;

public class SanctionCommand extends BaseCommand {

    private final SanctionsManager sanctionsManager;

    public SanctionCommand(Bantidito plugin) {
        this.sanctionsManager = plugin.getSanctionsManager();
    }

    @Override
    @Command
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            SenderUtil.sendMessage(sender, Language.SANCTION_COMMAND_CORRECT_USAGE.replace("<command>", command.getLabel()));
            return;
        }

        SanctionsMenu sanctionsMenu = new SanctionsMenu(args[0], sanctionsManager);
        sanctionsMenu.openMenu(sender);
    }
}
