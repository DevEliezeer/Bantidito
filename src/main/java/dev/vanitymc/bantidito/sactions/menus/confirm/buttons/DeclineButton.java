package dev.vanitymc.bantidito.sactions.menus.confirm.buttons;

import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.sactions.SanctionsManager;
import dev.vanitymc.bantidito.utilities.item.ItemBuilder;
import dev.vanitymc.bantidito.utilities.menu.Button;
import dev.vanitymc.bantidito.sactions.menus.sanction.SanctionsMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class DeclineButton extends Button {

    private final SanctionsManager sanctionsManager;
    private final String target;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Config.DECLINE_BUTTON_ICON_MATERIAL)
                .setData(Config.DECLINE_BUTTON_ICON_DATA)
                .setDisplayName(Config.DECLINE_BUTTON_ICON_DISPLAY_NAME)
                .setLore(Config.DECLINE_BUTTON_ICON_LORE)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playFail(player);
        new SanctionsMenu(target, sanctionsManager).openMenu(player);
    }
}