package dev.vanitymc.bantidito.sactions;

import dev.vanitymc.bantidito.sactions.enums.SanctionType;
import dev.vanitymc.bantidito.sactions.variations.SanctionVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;

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
        player.performCommand("ban -s " + target + " " + reason);
    }

    private void performTempBan(Player player, String target, String reason, String duration) {
        player.performCommand("tempban -s " + target + " " + reason + " " + duration);
    }

    private void performMute(Player player, String target, String reason) {
        player.performCommand("mute -s " + target + " " + reason);
    }

    private void performTempMute(Player player, String target, String reason, String duration) {
        player.performCommand("tempmute -s " + target + " " + reason + " " + duration);
    }

    private void performWarn(Player player, String target, String reason) {
        player.performCommand("warn -s " + target + " " + reason);
    }
}
