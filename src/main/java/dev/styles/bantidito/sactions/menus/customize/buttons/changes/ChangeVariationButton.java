package dev.styles.bantidito.sactions.menus.customize.buttons.changes;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.variations.SanctionVariant;
import dev.styles.bantidito.sactions.variations.SanctionVariantManager;
import dev.styles.bantidito.sactions.SanctionCustom;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChangeVariationButton extends Button {

    private final SanctionVariantManager sanctionVariantManager;
    private final SanctionCustom sanction;

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