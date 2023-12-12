package dev.styles.bantidito.sactions;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.enums.SanctionType;
import dev.styles.bantidito.sactions.variations.SanctionVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Getter @Setter
@AllArgsConstructor
public class SanctionCustom {

    private String target;

    private ItemStack icon;
    private String name;
    private SanctionType type;
    private String reason;
    private String duration;
    private ArrayList<SanctionVariant> variations;

    private boolean admittedSettings;
    private String admittedDuration;
    private String unadmittedDuration;

    public void sanctionPlayer(Player player, String target) {
        switch (type) {
            case BAN:
                if (duration.equalsIgnoreCase("PERMANENT")) {
                    performBan(player, target, reason);
                } else {
                    performTempBan(player, target, reason, duration);
                }
                break;
            case MUTE:
                if (duration.equalsIgnoreCase("PERMANENT")) {
                    performMute(player, target, reason);
                } else {
                    performTempMute(player, target, reason, duration);
                }
                break;
            case WARN:
                performWarn(player, target, reason);
                break;
            default:
                break;
        }
    }

    private void performBan(Player player, String target, String reason) {
        player.performCommand(Config.SANCTION_SETTINGS_BAN_COMMAND
                .replace("<player>", target)
                .replace("<reason>", reason)
        );
    }

    private void performTempBan(Player player, String target, String reason, String duration) {
        player.performCommand(Config.SANCTION_SETTINGS_TEMPBAN_COMMAND
                .replace("<player>", target)
                .replace("<reason>", reason)
                .replace("<duration>", duration)
        );
    }

    private void performMute(Player player, String target, String reason) {
        player.performCommand(Config.SANCTION_SETTINGS_MUTE_COMMAND
                .replace("<player>", target)
                .replace("<reason>", reason)
        );
    }

    private void performTempMute(Player player, String target, String reason, String duration) {
        player.performCommand(Config.SANCTION_SETTINGS_TEMPMUTE_COMMAND
                .replace("<player>", target)
                .replace("<reason>", reason)
                .replace("<duration>", duration)
        );
    }

    private void performWarn(Player player, String target, String reason) {
        player.performCommand(Config.SANCTION_SETTINGS_WARN_COMMAND
                .replace("<player>", target)
                .replace("<reason>", reason)
        );
    }
}
