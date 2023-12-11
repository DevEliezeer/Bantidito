package dev.styles.bantidito.sactions;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.config.Language;
import dev.styles.bantidito.sactions.enums.SanctionType;
import dev.styles.bantidito.sactions.variations.SanctionVariant;
import dev.styles.bantidito.sactions.variations.SanctionVariantManager;
import dev.styles.bantidito.utilities.SenderUtil;
import dev.styles.bantidito.utilities.ServerUtil;
import dev.styles.bantidito.utilities.StringUtil;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.Bantidito;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class SanctionsManager {

    private final Bantidito plugin;

    @Getter private final SanctionVariantManager sanctionVariantManager;

    @Getter private final Set<Sanction> sanctions;
    @Getter private final Map<Player, SanctionCustom> editingPlayers;
    @Getter private final Set<Player> admittedToggles;

    public SanctionsManager(Bantidito plugin) {
        this.plugin = plugin;
        this.sanctionVariantManager = new SanctionVariantManager();
        this.sanctions = new HashSet<>();
        this.editingPlayers = new HashMap<>();
        this.admittedToggles = new HashSet<>();

        loadSanctions();
    }

    private void loadSanctions() {
        for (String sanction : Config.SANCTIONS.getKeys(false)) {
            int slot = Config.SANCTIONS.getInt(sanction + ".SLOT");
            SanctionType type = SanctionType.valueOf(Config.SANCTIONS.getString(sanction + ".TYPE"));
            String reason = Config.SANCTIONS.getString(sanction + ".REASON");
            String duration = Config.SANCTIONS.getString(sanction + ".DURATION");

            String material = Config.SANCTIONS.getString(sanction + ".ICON.MATERIAL");
            int data = Config.SANCTIONS.getInt(sanction + ".ICON.DATA");
            String displayName = Config.SANCTIONS.getString(sanction + ".ICON.DISPLAY_NAME")
                    .replace("<type>", StringUtil.capitalizeFirstLetter(String.valueOf(type)))
                    .replace("<reason>", reason)
                    .replace("<duration>", StringUtil.capitalizeFirstLetter(String.valueOf(duration)));

            List<String> lore = Config.SANCTIONS.getStringList(sanction + ".ICON.LORE");
            lore.replaceAll(line -> line
                    .replace("<type>", StringUtil.capitalizeFirstLetter(String.valueOf(type)))
                    .replace("<reason>", reason)
                    .replace("<duration>", StringUtil.capitalizeFirstLetter(String.valueOf(duration))));

            ArrayList<SanctionVariant> variants = new ArrayList<>();
            ConfigurationSection variantsSection = Config.SANCTIONS.getConfigurationSection(sanction + ".VARIATIONS");
            if (variantsSection != null) {
                for (String variant : variantsSection.getKeys(false)) {
                    if (variant != null) {
                        variants.add(new SanctionVariant(variantsSection.getString(variant + ".DISPLAY")));
                    }
                }
            }

            boolean isAdmittedSettings = Config.SANCTIONS.getConfigurationSection(sanction + ".ADMITTED_SETTINGS") != null;

            sanctions.add(new Sanction(
                    slot,
                    new ItemBuilder(material)
                            .setData(data)
                            .setDisplayName(displayName)
                            .setLore(lore)
                            .build(),
                    sanction,
                    type,
                    reason,
                    duration,
                    variants,
                    isAdmittedSettings
            ));
        }
    }

    public void onReload() {
        sanctions.clear();
        loadSanctions();
    }

    public void setEditingReason(Player player, SanctionCustom sanctionCustom) {
        SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_EDITING_REASON);

        player.setMetadata("sanction.editing.reason", new FixedMetadataValue(plugin, true));
        editingPlayers.put(player, sanctionCustom);
    }

    public void setEditingDuration(Player player, SanctionCustom sanctionCustom) {
        SenderUtil.sendMessage(player, Language.SANCTION_CUSTOMIZE_EDITING_DURATION);

        player.setMetadata("sanction.editing.duration", new FixedMetadataValue(plugin, true));
        editingPlayers.put(player, sanctionCustom);
    }

    public void removeEditingReason(Player player) {
        player.removeMetadata("sanction.editing.reason", plugin);
        editingPlayers.remove(player);
    }

    public void removeEditingDuration(Player player) {
        player.removeMetadata("sanction.editing.duration", plugin);
        editingPlayers.remove(player);
    }

    public boolean isEditingMode(Player player) {
        return editingPlayers.containsKey(player);
    }

    public boolean isEditingReason(Player player) {
        return player.hasMetadata("sanction.editing.reason") && editingPlayers.containsKey(player);
    }

    public boolean isEditingDuration(Player player) {
        return player.hasMetadata("sanction.editing.duration") && editingPlayers.containsKey(player);
    }

    public void setAdmittedToggle(Player player) {
        admittedToggles.add(player);
    }

    public void removeAdmittedToggle(Player player) {
        admittedToggles.remove(player);
    }

    public boolean isAdmittedToggle(Player player) {
        return admittedToggles.contains(player);
    }
}