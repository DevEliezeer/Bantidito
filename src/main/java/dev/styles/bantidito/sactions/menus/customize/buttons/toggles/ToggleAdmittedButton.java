package dev.styles.bantidito.sactions.menus.customize.buttons.toggles;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import dev.styles.bantidito.sactions.SanctionCustom;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ToggleAdmittedButton extends Button {

    private final SanctionsManager sanctionsManager;
    private final SanctionCustom sanctionCustom;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack button;

        if (!sanctionsManager.isAdmittedToggle(player)) {
            button = new ItemBuilder(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_UNADMITTED_BUTTON_MATERIAL)
                    .setData(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_UNADMITTED_BUTTON_DATA)
                    .setDisplayName(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_UNADMITTED_BUTTON_DISPLAY_NAME)
                    .setLore(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_UNADMITTED_BUTTON_LORE)
                    .build();
        } else {
            button = new ItemBuilder(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_ADMITTED_BUTTON_MATERIAL)
                    .setData(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_ADMITTED_BUTTON_DATA)
                    .setDisplayName(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_ADMITTED_BUTTON_DISPLAY_NAME)
                    .setLore(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_ICON_ADMITTED_BUTTON_LORE)
                    .build();
        }

        return button;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playNeutral(player);
        if (!sanctionsManager.isAdmittedToggle(player)) {
            sanctionsManager.setAdmittedToggle(player);
            sanctionCustom.setDuration(sanctionCustom.getAdmittedDuration());
        } else {
            sanctionsManager.removeAdmittedToggle(player);
            sanctionCustom.setDuration(sanctionCustom.getUnadmittedDuration());
        }
    }
}
