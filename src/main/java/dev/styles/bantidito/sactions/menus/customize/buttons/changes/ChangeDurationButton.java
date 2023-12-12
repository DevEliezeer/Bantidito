package dev.styles.bantidito.sactions.menus.customize.buttons.changes;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.sactions.SanctionCustom;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.utilities.StringUtil;
import dev.styles.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ChangeDurationButton extends Button {

    private final SanctionsManager sanctionsManager;

    private final SanctionCustom sanctionCustom;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>(Config.CUSTOMIZE_CHANGE_DURATION_BUTTON_ICON_LORE);
        lore.replaceAll(line -> line
                .replace("<duration>", StringUtil.capitalizeFirstLetter(sanctionCustom.getDuration())));

        return new ItemBuilder(Config.CUSTOMIZE_CHANGE_DURATION_BUTTON_ICON_MATERIAL)
                .setData(Config.CUSTOMIZE_CHANGE_DURATION_BUTTON_ICON_DATA)
                .setDisplayName(Config.CUSTOMIZE_CHANGE_DURATION_BUTTON_ICON_DISPLAY_NAME)
                .setLore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        sanctionsManager.setEditingDuration(player, sanctionCustom);
        playNeutral(player);
        close(player);
    }
}
