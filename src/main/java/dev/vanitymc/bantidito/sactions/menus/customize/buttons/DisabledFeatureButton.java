package dev.vanitymc.bantidito.sactions.menus.customize.buttons;

import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.utilities.item.ItemBuilder;
import dev.vanitymc.bantidito.utilities.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DisabledFeatureButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Config.CUSTOMIZE_DISABLED_FEATURE_ICON_MATERIAL)
                .setData(Config.CUSTOMIZE_DISABLED_FEATURE_ICON_DATA)
                .setDisplayName(Config.CUSTOMIZE_DISABLED_FEATURE_ICON_DISPLAY_NAME)
                .setLore(Config.CUSTOMIZE_DISABLED_FEATURE_ICON_LORE)
                .build();
    }
}
