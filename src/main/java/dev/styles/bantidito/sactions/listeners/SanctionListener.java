package dev.styles.bantidito.sactions.listeners;

import dev.styles.bantidito.config.Language;
import dev.styles.bantidito.utilities.StringUtil;
import dev.styles.bantidito.Bantidito;
import dev.styles.bantidito.sactions.SanctionCustom;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.menus.customize.CustomizeMenu;
import dev.styles.bantidito.utilities.SenderUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SanctionListener implements Listener {

    private final SanctionsManager sanctionsManager;

    public SanctionListener(Bantidito plugin) {
        this.sanctionsManager = plugin.getSanctionsManager();
    }

    @EventHandler
    private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (sanctionsManager.isEditingMode(player)) {
            event.setCancelled(true);
            SanctionCustom sanctionCustom = sanctionsManager.getEditingPlayers().get(player);

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                sanctionsManager.removeEditingReason(player);
                sanctionsManager.removeEditingDuration(player);

                new CustomizeMenu(sanctionsManager, sanctionCustom.getTarget(), sanctionCustom).openMenu(player);
                return;
            }

            if (sanctionsManager.isEditingReason(player)) {
                SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_CHANGED_REASON
                        .replace("<from>", sanctionCustom.getReason())
                        .replace("<to>", event.getMessage()));

                sanctionCustom.setReason(event.getMessage());
                sanctionsManager.removeEditingReason(player);
            } else if (sanctionsManager.isEditingDuration(player)) {
                String duration = event.getMessage().trim();

                if(duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm")) {
                    SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_CHANGED_DURATION
                            .replace("<from>", StringUtil.capitalizeFirstLetter(sanctionCustom.getDuration()))
                            .replace("<to>", "Permanent"));

                    sanctionCustom.setDuration("PERMANENT");
                    sanctionsManager.removeEditingDuration(player);
                    new CustomizeMenu(sanctionsManager, sanctionCustom.getTarget(), sanctionCustom).openMenu(player);
                    return;
                }

                if (!isValidDuration(duration)) {
                    SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_INVALID_DURATION);
                    return;
                }

                SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_CHANGED_DURATION
                        .replace("<from>", StringUtil.capitalizeFirstLetter(sanctionCustom.getDuration()))
                        .replace("<to>", event.getMessage()));

                sanctionCustom.setDuration(event.getMessage());
                sanctionsManager.removeEditingDuration(player);
            }

            new CustomizeMenu(sanctionsManager, sanctionCustom.getTarget(), sanctionCustom).openMenu(player);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        sanctionsManager.removeEditingReason(player);
        sanctionsManager.removeEditingDuration(player);
    }

    public boolean isValidDuration(String duration) {
        String valid = "\\d+(s|second|seconds|m|minute|minutes|h|hour|hours|d|day|days|w|week|weeks|mo|month|months|y|year|years)";

        Pattern pattern = Pattern.compile(valid);
        Matcher matcher = pattern.matcher(duration);

        return matcher.matches();
    }
}
