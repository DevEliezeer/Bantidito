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
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (sanctionsManager.isEditingMode(player)) {
            handleEditingMode(event, player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        clearEditingData(player);
    }

    private void handleEditingMode(AsyncPlayerChatEvent event, Player player) {
        event.setCancelled(true);
        SanctionCustom sanctionCustom = sanctionsManager.getEditingPlayers().get(player);

        if (event.getMessage().equalsIgnoreCase("cancel")) {
            handleCancelEditing(player, sanctionCustom);
            return;
        }

        if (sanctionsManager.isEditingReason(player)) {
            handleEditingReason(player, sanctionCustom, event.getMessage());
        } else if (sanctionsManager.isEditingDuration(player)) {
            handleEditingDuration(player, sanctionCustom, event.getMessage());
        }

        new CustomizeMenu(sanctionsManager, sanctionCustom.getTarget(), sanctionCustom).openMenu(player);
    }

    private void handleCancelEditing(Player player, SanctionCustom sanctionCustom) {
        sanctionsManager.removeEditingReason(player);
        sanctionsManager.removeEditingDuration(player);

        new CustomizeMenu(sanctionsManager, sanctionCustom.getTarget(), sanctionCustom).openMenu(player);
    }

    private void handleEditingReason(Player player, SanctionCustom sanctionCustom, String message) {
        SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_CHANGED_REASON
                .replace("<from>", StringUtil.capitalizeFirstLetter(sanctionCustom.getReason()))
                .replace("<to>", message));

        sanctionCustom.setReason(message);
        sanctionsManager.removeEditingReason(player);
    }

    private void handleEditingDuration(Player player, SanctionCustom sanctionCustom, String message) {
        String duration = message.trim();

        if (duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm")) {
            handlePermanentDuration(player, sanctionCustom);
            return;
        }

        if (!isValidDuration(duration)) {
            SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_INVALID_DURATION);
            return;
        }

        SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_CHANGED_DURATION
                .replace("<from>", StringUtil.capitalizeFirstLetter(sanctionCustom.getDuration()))
                .replace("<to>", duration));

        sanctionCustom.setDuration(duration);
        sanctionsManager.removeEditingDuration(player);
    }

    private void handlePermanentDuration(Player player, SanctionCustom sanctionCustom) {
        SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_CHANGED_DURATION
                .replace("<from>", StringUtil.capitalizeFirstLetter(sanctionCustom.getDuration()))
                .replace("<to>", "Permanent"));

        sanctionCustom.setDuration("PERMANENT");
        sanctionsManager.removeEditingDuration(player);
        new CustomizeMenu(sanctionsManager, sanctionCustom.getTarget(), sanctionCustom).openMenu(player);
    }

    private boolean isValidDuration(String duration) {
        String valid = "\\d+(s|second|seconds|m|minute|minutes|h|hour|hours|d|day|days|w|week|weeks|mo|month|months|y|year|years)";

        Pattern pattern = Pattern.compile(valid);
        Matcher matcher = pattern.matcher(duration);

        return matcher.matches();
    }

    private void clearEditingData(Player player) {
        sanctionsManager.removeEditingReason(player);
        sanctionsManager.removeEditingDuration(player);
    }
}