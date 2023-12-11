package dev.styles.bantidito;

import dev.styles.bantidito.commands.BantiditoCommand;
import dev.styles.bantidito.commands.SanctionCommand;
import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.config.Language;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.listeners.SanctionListener;
import dev.styles.bantidito.utilities.ServerUtil;
import dev.styles.bantidito.utilities.command.CommandManager;
import dev.styles.bantidito.utilities.file.FileConfig;
import dev.styles.bantidito.utilities.menu.listener.ButtonListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Bantidito extends JavaPlugin {

    @Getter private Map<String, FileConfig> files;

    private Config config;
    private Language language;

    @Getter private CommandManager commandManager;
    @Getter private SanctionsManager sanctionsManager;

    @Override
    public void onEnable() {
        ServerUtil.logger("");
        ServerUtil.logger("       &3&lBantidito&7 - " + getDescription().getVersion());
        ServerUtil.logger("");
        ServerUtil.logger("&aConnecting to the license server...");

        if (true) {
            try {
                ServerUtil.logger("");
                ServerUtil.logger("&aThe license was successfully validated");
                ServerUtil.logger("&aEnabling Bantidito...");
                ServerUtil.logger("");

                loadFiles();
                loadManagers();
                loadListeners();
                loadCommands();
            } catch (Exception exception) {
                exception.printStackTrace();

                ServerUtil.logger("");
                ServerUtil.logger("&cAn unexpected error occurred while enabling the plugin");
                ServerUtil.logger("&cDisabling Bantidito...");
                ServerUtil.logger("");
            }
        } else {
            ServerUtil.logger("");
            ServerUtil.logger("&cThe license is invalid.");
            ServerUtil.logger("&cDisabling Bantidito...");
            ServerUtil.logger("");
        }

        ServerUtil.logger("  &7&oStyles Development @ discord.styles.dev");
        ServerUtil.logger("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void onReload() {
        files.values().forEach(FileConfig::reload);

        config.initialize(files);
        language.initialize(files);

        sanctionsManager.onReload();
    }

    private void loadFiles() {
        files = new HashMap<>();

        config = new Config(this, files);
        language = new Language(this, files);
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
