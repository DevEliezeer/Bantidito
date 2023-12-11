package dev.styles.bantidito.utilities.menu.pagination;

import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PageButton extends Button {

    private final int mod;
    private final PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(XMaterial.LEVER.parseMaterial());

        if (hasNext(player)) {
            itemBuilder.setDisplayName(mod > 0 ? "&aNext page" : "&cPrevious page");
        }
        else {
            itemBuilder.setDisplayName("&7" + (mod > 0 ? "Last page" : "First page"));
        }

        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (hasNext(player)) {
            menu.modPage(player, mod);
            playNeutral(player);
        }
        else {
            playFail(player);
        }
    }

    private boolean hasNext(Player player) {
        int pg = menu.getPage() + mod;
        return pg > 0 && menu.getPages(player) >= pg;
    }
}