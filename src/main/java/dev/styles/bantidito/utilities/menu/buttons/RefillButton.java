package dev.styles.bantidito.utilities.menu.buttons;

import com.cryptomorin.xseries.XMaterial;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class RefillButton extends Button {

    private final int data;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(XMaterial.GLASS_PANE.parseMaterial())
                .setData(data)
                .setDisplayName("&7")
                .build();
    }
}
