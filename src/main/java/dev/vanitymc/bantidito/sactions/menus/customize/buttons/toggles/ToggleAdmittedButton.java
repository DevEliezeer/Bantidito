package dev.vanitymc.bantidito.sactions.menus.customize.buttons.toggles;

import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.sactions.SanctionCustom;
import dev.vanitymc.bantidito.sactions.SanctionsManager;
import dev.vanitymc.bantidito.utilities.item.ItemBuilder;
import dev.vanitymc.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ToggleAdmittedButton extends Button {

    private final SanctionsManager sanctionsManager;
    private final SanctionCustom sanctionCustom;

    private String admittedDuration;
    private String unadmittedDuration;

    public ToggleAdmittedButton(SanctionsManager sanctionsManager, SanctionCustom sanctionCustom) {
        this.sanctionsManager = sanctionsManager;
        this.sanctionCustom = sanctionCustom;

        if (sanctionCustom.isAdmittedSettings()) {
            this.admittedDuration = Config.SANCTIONS.getString(sanctionCustom.getName() + ".ADMITTED_SETTINGS.ADMITTED_DURATION");
            this.unadmittedDuration = Config.SANCTIONS.getString(sanctionCustom.getName() + ".ADMITTED_SETTINGS.UNADMITTED_DURATION");
        }
    }

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
            sanctionCustom.setDuration(admittedDuration);
        } else {
            sanctionsManager.removeAdmittedToggle(player);
            sanctionCustom.setDuration(unadmittedDuration);
        }
    }
}
