package dev.styles.bantidito.sactions.menus.sanction.buttons;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InfoButton extends Button {

    private final String target;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Config.SANCTIONS_PLAYER_INFO_ICON_MATERIAL)
                .setData(Config.SANCTIONS_PLAYER_INFO_ICON_DATA)
                .setSkullOwner(Config.SANCTIONS_PLAYER_INFO_ICON_HEAD.replace("<target>", target))
                .setDisplayName(Config.SANCTIONS_PLAYER_INFO_ICON_DISPLAY_NAME.replace("<target>", target))
                .setLore(Config.SANCTIONS_PLAYER_INFO_ICON_LORE.stream().map(line -> line.replace("<target>", target)).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        player.performCommand(Config.SANCTION_SETTINGS_HISTORY_COMMAND.replace("<player>", target));
        playNeutral(player);
        close(player);
    }
}