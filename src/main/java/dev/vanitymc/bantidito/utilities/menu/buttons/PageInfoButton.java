package dev.vanitymc.bantidito.utilities.menu.buttons;

import dev.vanitymc.bantidito.utilities.item.ItemBuilder;
import dev.vanitymc.bantidito.utilities.menu.Button;
import dev.vanitymc.bantidito.utilities.menu.pagination.PaginatedMenu;
import com.cryptomorin.xseries.XMaterial;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageInfoButton extends Button {

    private final PaginatedMenu paginatedMenu;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(XMaterial.NETHER_STAR.parseMaterial())
                .setDisplayName("&ePage Info")
                .setLore("&e" + paginatedMenu.getPage() + "&7/&a" + paginatedMenu.getPages(player))
                .build();
    }
}