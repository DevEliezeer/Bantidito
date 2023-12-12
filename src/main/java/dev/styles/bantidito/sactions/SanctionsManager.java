package dev.styles.bantidito.sactions;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.config.Language;
import dev.styles.bantidito.sactions.enums.SanctionType;
import dev.styles.bantidito.sactions.variations.SanctionVariant;
import dev.styles.bantidito.sactions.variations.SanctionVariantManager;
import dev.styles.bantidito.utilities.SenderUtil;
import dev.styles.bantidito.utilities.StringUtil;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.Bantidito;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SanctionsManager {

    private final Bantidito plugin;

    @Getter private final SanctionVariantManager sanctionVariantManager;

    @Getter private final Set<Sanction> sanctions;
    @Getter private final Set<SanctionPage> sanctionPages;
    @Getter private final Map<Player, SanctionCustom> editingPlayers;
    @Getter private final Set<Player> admittedToggles;

    public SanctionsManager(Bantidito plugin) {
        this.plugin = plugin;
        this.sanctionVariantManager = new SanctionVariantManager();
        this.sanctions = new HashSet<>();
        this.sanctionPages = new HashSet<>();
        this.editingPlayers = new HashMap<>();
        this.admittedToggles = new HashSet<>();

        loadSanctions();
    }

    private void loadSanctions() {
        for (String page : Config.SANCTIONS_PAGES.getKeys(false)) {
            int pageId = Config.SANCTIONS_PAGES.getInt(page + ".PAGE");

            ConfigurationSection pageSection = Config.SANCTIONS_PAGES.getConfigurationSection(page + ".SANCTIONS");
            for (String sanction : pageSection.getKeys(false)) {
                int slot = pageSection.getInt(sanction + ".SLOT");
                SanctionType type = SanctionType.valueOf(pageSection.getString(sanction + ".TYPE"));
                String reason = pageSection.getString(sanction + ".REASON");
                String duration = pageSection.getString(sanction + ".DURATION");

                String material = pageSection.getString(sanction + ".ICON.MATERIAL");
                int data = pageSection.getInt(sanction + ".ICON.DATA");
                String displayName = pageSection.getString(sanction + ".ICON.DISPLAY_NAME")
                        .replace("<type>", StringUtil.capitalizeFirstLetter(String.valueOf(type)))
                        .replace("<reason>", reason)
                        .replace("<duration>", StringUtil.capitalizeFirstLetter(String.valueOf(duration)));

                List<String> lore = pageSection.getStringList(sanction + ".ICON.LORE");
                replaceVariables(lore, type, reason, duration);

                ArrayList<SanctionVariant> variants = new ArrayList<>();
                ConfigurationSection variantsSection = pageSection.getConfigurationSection(sanction + ".VARIATIONS");
                if (variantsSection != null) {
                    for (String variant : variantsSection.getKeys(false)) {
                        if (variant != null) {
                            variants.add(new SanctionVariant(variantsSection.getString(variant + ".DISPLAY")));
                        }
                    }
                }

                boolean isAdmittedSettings = pageSection.getConfigurationSection(sanction + ".ADMITTED_SETTINGS") != null;
                String admittedDuration = isAdmittedSettings ? pageSection.getString(sanction + ".ADMITTED_SETTINGS.ADMITTED_DURATION") : null;
                String unadmittedDuration = isAdmittedSettings ? pageSection.getString(sanction + ".ADMITTED_SETTINGS.UNADMITTED_DURATION") : null;

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
                        isAdmittedSettings,
                        admittedDuration,
                        unadmittedDuration));
            }

            sanctionPages.add(new SanctionPage(pageId, new HashSet<>(sanctions)));
            sanctions.clear();
        }
    }

    private void replaceVariables(List<String> list, SanctionType type, String reason, String duration) {
        list.replaceAll(line -> line
                .replace("<type>", StringUtil.capitalizeFirstLetter(String.valueOf(type)))
                .replace("<reason>", reason)
                .replace("<duration>", StringUtil.capitalizeFirstLetter(String.valueOf(duration))));
    }

    public void onReload() {
        sanctions.clear();
        sanctionPages.clear();
        loadSanctions();
    }

    public SanctionPage getSanctionPage(int page) {
        return sanctionPages.stream()
                .filter(sanctionPage -> sanctionPage.getPage() == page)
                .findFirst()
                .orElse(null);
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