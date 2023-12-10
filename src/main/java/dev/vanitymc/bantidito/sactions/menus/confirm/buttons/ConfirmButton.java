package dev.vanitymc.bantidito.sactions.menus.confirm.buttons;

import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.sactions.SanctionCustom;
import dev.vanitymc.bantidito.utilities.item.ItemBuilder;
import dev.vanitymc.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ConfirmButton extends Button {

    private final String target;
    private final SanctionCustom sanction;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Config.CONFIRM_BUTTON_ICON_MATERIAL)
                .setData(Config.CONFIRM_BUTTON_ICON_DATA)
                .setDisplayName(Config.CONFIRM_BUTTON_ICON_DISPLAY_NAME)
                .setLore(Config.CONFIRM_BUTTON_ICON_LORE)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        sanction.sanctionPlayer(player, target);
        playSuccess(player);
        close(player);
    }
}