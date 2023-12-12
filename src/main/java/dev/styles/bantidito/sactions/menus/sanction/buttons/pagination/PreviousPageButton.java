package dev.styles.bantidito.sactions.menus.sanction.buttons.pagination;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.SanctionPage;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.menus.sanction.SanctionsMenu;
import dev.styles.bantidito.utilities.SenderUtil;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PreviousPageButton extends Button {

    private final SanctionsMenu sanctionsMenu;
    private final SanctionsManager sanctionsManager;
    private final SanctionPage sanctionPage;
    private final String target;

    public PreviousPageButton(SanctionsMenu sanctionsMenu) {
        this.sanctionsMenu = sanctionsMenu;
        this.sanctionsManager = sanctionsMenu.getSanctionsManager();
        this.sanctionPage = sanctionsMenu.getSanctionPage();
        this.target = sanctionsMenu.getTarget();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Config.SANCTIONS_PREVIOUS_PAGE_BUTTON_ICON_MATERIAL)
                .setData(Config.SANCTIONS_PREVIOUS_PAGE_BUTTON_ICON_DATA)
                .setDisplayName(Config.SANCTIONS_PREVIOUS_PAGE_BUTTON_ICON_DISPLAY_NAME)
                .setLore(Config.SANCTIONS_PREVIOUS_PAGE_BUTTON_ICON_LORE)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        SanctionPage previousPage = sanctionsManager.getSanctionPage(sanctionPage.getPage() - 1);

        if (previousPage != null) {
            playNeutral(player);
            new SanctionsMenu(previousPage, target, sanctionsManager).openMenu(player);
        } else {
            playFail(player);
            SenderUtil.sendMessage(player, "&cYou have reached the limit of previous pages.");
        }
    }
}
