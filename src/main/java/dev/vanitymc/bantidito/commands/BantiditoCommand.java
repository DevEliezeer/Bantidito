package dev.vanitymc.bantidito.commands;

import dev.vanitymc.bantidito.Bantidito;
import dev.vanitymc.bantidito.utilities.ColorUtil;
import dev.vanitymc.bantidito.utilities.command.BaseCommand;
import dev.vanitymc.bantidito.utilities.command.Command;
import dev.vanitymc.bantidito.utilities.command.CommandArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class BantiditoCommand extends BaseCommand {

    private final Bantidito plugin;

    @Override
    @Command(name = "bantidito")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(ColorUtil.translate(new String[]{
                "",
                "     &5&lBan&d&ltidito &7- " + plugin.getDescription().getVersion(),
                "",
                " &7● &fVersion&7: &d" + plugin.getDescription().getVersion(),
                " &7● &fAuthor&7: &d" + plugin.getDescription().getAuthors(),
                "",
                " &7&oVanityMC Development @ vanitymc.es",
                ""
        }));
    }
}
