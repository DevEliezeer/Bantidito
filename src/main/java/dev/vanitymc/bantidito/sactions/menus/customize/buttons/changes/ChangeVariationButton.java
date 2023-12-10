package dev.vanitymc.bantidito.sactions.menus.customize.buttons.changes;

import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.sactions.SanctionCustom;
import dev.vanitymc.bantidito.sactions.SanctionsManager;
import dev.vanitymc.bantidito.sactions.variations.SanctionVariant;
import dev.vanitymc.bantidito.sactions.variations.SanctionVariantManager;
import dev.vanitymc.bantidito.utilities.item.ItemBuilder;
import dev.vanitymc.bantidito.utilities.menu.Button;
import dev.vanitymc.bantidito.utilities.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeVariationButton extends Button {

    private final SanctionsManager sanctionsManager;
    private final SanctionVariantManager sanctionVariantManager;

    private final String target;
    private final SanctionCustom sanction;

    public ChangeVariationButton(SanctionsManager sanctionsManager, SanctionVariantManager sanctionVariantManager, String target, SanctionCustom sanction) {
        this.sanctionsManager = sanctionsManager;
        this.sanctionVariantManager = sanctionVariantManager;
        this.target = target;
        this.sanction = sanction;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> variants = new ArrayList<>();
        List<SanctionVariant> variationsList = new ArrayList<>(sanction.getVariations());

        for (SanctionVariant variant : variationsList) {
            variants.add(Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_ICON_LORE_FORMAT
                    .replace("<status>", sanctionVariantManager.hasSelected(player, variant) ? "&a" : "&7")
                    .replace("<variation>", variant.getDisplay())
                    .replace("(", "")
                    .replace(")", ""));
        }

        List<String> lore = new ArrayList<>();
        for (String line : Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_ICON_LORE) {
            lore.add(line);
            if (line.equalsIgnoreCase("<variations>"))  {
                lore.addAll(variants);
            }
        }

        return new ItemBuilder(Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_ICON_MATERIAL)
                .setData(Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_ICON_DATA)
                .setDisplayName(Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_ICON_DISPLAY_NAME)
                .setLore(lore.stream().map(line -> replacePlaceholders(player, line)).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playNeutral(player);

        List<SanctionVariant> variations = sanction.getVariations();
        if (!variations.isEmpty()) {
            int currentIndex = sanctionVariantManager.getCurrentIndex().getOrDefault(player, 0);

            if (currentIndex < variations.size()) {
                SanctionVariant sanctionVariant = variations.get(currentIndex);
                sanctionVariantManager.getSelected().put(player, sanctionVariant);

                sanction.setReason(getReplacedReason(sanction.getReason()));
                sanction.setReason(sanction.getReason() + " " + sanctionVariant.getDisplay());

                sanctionVariantManager.getCurrentIndex().put(player, currentIndex + 1);
            } else {
                sanctionVariantManager.getCurrentIndex().remove(player);
                sanctionVariantManager.getCurrentIndex().put(player, 0);

                sanction.setReason(getReplacedReason(sanction.getReason()));

                sanctionVariantManager.getSelected().remove(player);
            }
        }
    }

    private String replacePlaceholders(Player player, String string) {
        return string
                .replace("<variations>", Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_ICON_LORE_FORMAT
                        .replace("<status>", !sanctionVariantManager.hasSelected(player) ? "&a" : "&7")
                        .replace("<variation>", ChatColor.GRAY + "None"));
    }

    private String getReplacedReason(String string) {
        for (SanctionVariant variant : sanction.getVariations()) {
            if (string.contains(variant.getDisplay())) {
                return string.replace(" "  + variant.getDisplay(), "");
            }
        }

        return string;
    }
}