package dev.styles.bantidito.sactions.menus.confirm.buttons;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.SanctionCustom;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.menus.customize.CustomizeMenu;
import dev.styles.bantidito.utilities.StringUtil;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class InfoButton extends Button {

    private final SanctionsManager sanctionsManager;

    private final String target;
    private final SanctionCustom sanctionCustom;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>(Config.CONFIRM_SANCTION_INFO_ICON_LORE);
        lore.replaceAll(line -> line
                .replace("<target>", target)
                .replace("<type>", StringUtil.capitalizeFirstLetter(String.valueOf(sanctionCustom.getType())))
                .replace("<reason>", sanctionCustom.getReason())
                .replace("<duration>", StringUtil.capitalizeFirstLetter(sanctionCustom.getDuration())));

        return new ItemBuilder(Config.CONFIRM_SANCTION_INFO_ICON_MATERIAL)
                .setData(Config.CONFIRM_SANCTION_INFO_ICON_DATA)
                .setSkullOwner(Config.CONFIRM_SANCTION_INFO_ICON_HEAD.replace("<target>", target))
                .setDisplayName(Config.CONFIRM_SANCTION_INFO_ICON_DISPLAY_NAME.replace("<target>", target))
                .setLore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        close(player);
        CustomizeMenu customizeMenu = new CustomizeMenu(sanctionsManager, target, sanctionCustom);
        customizeMenu.openMenu(player);
        playNeutral(player);
    }
}
