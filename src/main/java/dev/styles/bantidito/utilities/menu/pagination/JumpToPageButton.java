package dev.styles.bantidito.utilities.menu.pagination;

import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.ColorUtil;
import dev.styles.bantidito.utilities.menu.Button;
import com.cryptomorin.xseries.XMaterial;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class JumpToPageButton extends Button {

    private final int page;
    private final PaginatedMenu menu;
    private final boolean current;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(this.current ? XMaterial.ENCHANTED_BOOK.parseMaterial() : XMaterial.BOOK.parseMaterial(), this.page);
        itemBuilder.setDisplayName(ColorUtil.translate("&cPage " + this.page));

        if (this.current) {
            itemBuilder.setLore(
                    "",
                    ColorUtil.translate("&aCurrent page")
            );
        }

        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        playNeutral(player);
    }
}