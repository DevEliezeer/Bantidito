package dev.styles.bantidito.sactions.menus.sanction.buttons;

import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.menus.confirm.ConfirmMenu;
import dev.styles.bantidito.sactions.menus.customize.CustomizeMenu;
import dev.styles.bantidito.utilities.menu.Button;
import dev.styles.bantidito.sactions.Sanction;
import dev.styles.bantidito.sactions.SanctionCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SanctionButton extends Button {

    private final SanctionsManager sanctionsManager;

    private final String target;
    private final Sanction sanction;

    private SanctionCustom sanctionCustom;

    public SanctionButton(SanctionsManager sanctionsManager, String target, Sanction sanction) {
        this.sanctionsManager = sanctionsManager;
        this.target = target;
        this.sanction = sanction;

        this.sanctionCustom = new SanctionCustom(
                target,
                sanction.getIcon(),
                sanction.getName(),
                sanction.getType(),
                sanction.getReason(),
                sanction.getDuration(),
                sanction.getVariations(),
                sanction.isAdmittedSettings(),
                sanction.getAdmittedDuration(),
                sanction.getUnadmittedDuration()
        );
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return sanction.getIcon();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType.isLeftClick()) {
            ConfirmMenu confirmMenu = new ConfirmMenu(sanctionsManager, target, sanctionCustom);
            confirmMenu.openMenu(player);
            playNeutral(player);
        } else if (clickType.isRightClick()) {
            CustomizeMenu customizeMenu = new CustomizeMenu(sanctionsManager, target, sanctionCustom);
            customizeMenu.openMenu(player);
            playNeutral(player);
        }
    }
}
