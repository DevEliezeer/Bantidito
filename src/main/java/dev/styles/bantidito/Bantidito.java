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
import dev.styles.bantidito.utilities.plugin.License;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
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
        ServerUtil.logger(new String[]{
                "",
                "       &3&lBantidito&7 - " + getDescription().getVersion(),
                "",
                "&aConnecting to the license server..."
        });

        loadFiles();
        License license = new License(URI.create("http://89.46.2.47:3000/api/client"), Config.LICENSE_KEY, getDescription().getName(), getDescription().getVersion(), "qdt7yw00HjQPIsY", null);
        if (license.check()) {
        //if (true) {
            if (license.isValid()) {
            //if (true) {
                ServerUtil.logger(new String[]{
                        "",
                        "&aThe license was successfully validated",
                        "&aEnabling Bantidito...",
                        "",
                        "&3Discord Name&7: &f" + license.getDiscordName(),
                        "&3Discord ID&7: &f" + license.getDiscordID(),
                        ""
                });

                try {
                    loadManagers();
                    loadListeners();
                    loadCommands();
                } catch (Exception exception) {
                    exception.printStackTrace();

                    ServerUtil.logger(new String[]{
                            "",
                            "&cAn unexpected error occurred while enabling the plugin",
                            "&cDisabling Bantidito...",
                            ""
                    });
                }
            } else {
                ServerUtil.logger(new String[]{
                        "",
                        "&cInvalid license, open a ticket to claim your license",
                        "&cDisabling Bantidito...",
                        ""
                });
            }
        }

        ServerUtil.logger(new String[]{
                "  &7&oStyles Development @ discord.styles.dev",
                ""
        });
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
