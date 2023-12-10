package dev.vanitymc.bantidito;

import dev.vanitymc.bantidito.config.Language;
import dev.vanitymc.bantidito.utilities.menu.listener.ButtonListener;
import dev.vanitymc.bantidito.commands.BantiditoCommand;
import dev.vanitymc.bantidito.commands.SanctionCommand;
import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.sactions.SanctionsManager;
import dev.vanitymc.bantidito.sactions.listeners.SanctionListener;
import dev.vanitymc.bantidito.utilities.ServerUtil;
import dev.vanitymc.bantidito.utilities.command.CommandManager;
import dev.vanitymc.bantidito.utilities.file.FileConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class Bantidito extends JavaPlugin {

    private Map<String, FileConfig> files;

    private CommandManager commandManager;
    private SanctionsManager sanctionsManager;

    @Override
    public void onEnable() {
        ServerUtil.logger("");
        ServerUtil.logger("      &5&l&nBan&d&l&ntidito&7 - " + getDescription().getVersion());

        if (Bukkit.getPluginManager().getPlugin("LiteBans") != null) {
            ServerUtil.logger("");
            ServerUtil.logger("&aSuccessfully hooked with LiteBans");
            ServerUtil.logger("&aEnabling Bantidito...");
            ServerUtil.logger("");

            loadFiles();
            loadManagers();
            loadListeners();
            loadCommands();
        } else {
            ServerUtil.logger("");
            ServerUtil.logger("&cAn error occurred while trying to hook with LiteBans");
            ServerUtil.logger("&cDisabling Bantidito...");
            ServerUtil.logger("");
        }

        ServerUtil.logger("  &7&oVanityMC Development @ vanitymc.es");
        ServerUtil.logger("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadFiles() {
        files = new HashMap<>();

        new Config(this, files);
        new Language(this, files);
    }

    private void loadManagers() {
        commandManager = new CommandManager(this);
        sanctionsManager = new SanctionsManager(this);
    }

    private void loadListeners() {
        Arrays.asList(
                new SanctionListener(this),
                new ButtonListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
    private void loadCommands() {
        commandManager.registerCommands(new BantiditoCommand(this));
        commandManager.registerCommands(new SanctionCommand(this),
                Config.COMMAND_SETTINGS_NAME,
                Config.COMMAND_SETTINGS_ALIASES,
                Config.COMMAND_SETTINGS_PERMISSION
        );
    }
}
