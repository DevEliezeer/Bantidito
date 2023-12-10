package dev.vanitymc.bantidito.utilities.menu.buttons;

import dev.vanitymc.bantidito.utilities.menu.Button;
import dev.vanitymc.bantidito.utilities.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BackButton extends Button {

    private final Menu back;
    private final ItemStack itemStack;

    @Override
    public ItemStack getButtonItem(Player player) {
        return itemStack;
    }

    @Override
    public void clicked(Player player, int n, ClickType clickType, int hb) {
        playNeutral(player);
        this.back.openMenu(player);
    }
}