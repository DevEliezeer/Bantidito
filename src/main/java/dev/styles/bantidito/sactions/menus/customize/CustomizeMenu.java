package dev.styles.bantidito.sactions.menus.customize;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.menus.customize.buttons.DisabledFeatureButton;
import dev.styles.bantidito.sactions.menus.customize.buttons.SanctionInfoButton;
import dev.styles.bantidito.sactions.menus.customize.buttons.changes.ChangeDurationButton;
import dev.styles.bantidito.sactions.menus.customize.buttons.changes.ChangeReasonButton;
import dev.styles.bantidito.sactions.menus.customize.buttons.changes.ChangeVariationButton;
import dev.styles.bantidito.sactions.menus.customize.buttons.toggles.ToggleAdmittedButton;
import dev.styles.bantidito.sactions.variations.SanctionVariantManager;
import dev.styles.bantidito.utilities.item.ItemBuilder;
import dev.styles.bantidito.utilities.menu.Button;
import dev.styles.bantidito.utilities.menu.Menu;
import dev.styles.bantidito.utilities.menu.buttons.BackButton;
import dev.styles.bantidito.utilities.menu.buttons.RefillButton;
import dev.styles.bantidito.sactions.SanctionCustom;
import dev.styles.bantidito.sactions.menus.sanction.SanctionsMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CustomizeMenu extends Menu {

    private final SanctionsManager sanctionsManager;
    private final SanctionVariantManager sanctionVariantManager;

    private final String target;
    private final SanctionCustom sanctionCustom;

    public CustomizeMenu(SanctionsManager sanctionsManager, String target, SanctionCustom sanctionCustom) {
        this.sanctionsManager = sanctionsManager;
        this.sanctionVariantManager = sanctionsManager.getSanctionVariantManager();
        this.target = target;
        this.sanctionCustom = sanctionCustom;

        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return Config.CUSTOMIZE_TITLE.replace("<target>", target);
    }

    @Override
    public int getSize() {
        return Config.CUSTOMIZE_ROWS * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (Config.CUSTOMIZE_REFILL_ENABLED) {
            for (int refill : Config.CUSTOMIZE_REFILL_SLOTS) {
                buttons.put(refill, new RefillButton(Config.CUSTOMIZE_REFILL_DATA));
            }
        }

        if (Config.CUSTOMIZE_BACK_BUTTON_ENABLED) {
            buttons.put(Config.CUSTOMIZE_BACK_BUTTON_SLOT, new BackButton(new SanctionsMenu(sanctionsManager.getSanctionPage(1), target, sanctionsManager), new ItemBuilder(
                    Config.CUSTOMIZE_BACK_BUTTON_ICON_MATERIAL)
                    .setData(Config.CUSTOMIZE_BACK_BUTTON_ICON_DATA)
                    .setDisplayName(Config.CUSTOMIZE_BACK_BUTTON_ICON_DISPLAY_NAME)
                    .setLore(Config.CUSTOMIZE_BACK_BUTTON_ICON_LORE)
                    .build()
            ));
        }

        buttons.put(Config.CUSTOMIZE_SANCTION_INFO_SLOT, new SanctionInfoButton(sanctionsManager, target, sanctionCustom));

        buttons.put(Config.CUSTOMIZE_CHANGE_REASON_BUTTON_SLOT, new ChangeReasonButton(sanctionsManager, target, sanctionCustom));
        buttons.put(Config.CUSTOMIZE_CHANGE_DURATION_BUTTON_SLOT, new ChangeDurationButton(sanctionsManager, target, sanctionCustom));
        buttons.put(Config.CUSTOMIZE_CHANGE_VARIATION_BUTTON_SLOT, new ChangeVariationButton(sanctionsManager, sanctionVariantManager, target, sanctionCustom));

        if (sanctionCustom.isAdmittedSettings()) {
            buttons.put(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_SLOT, new ToggleAdmittedButton(sanctionsManager, sanctionCustom));
        } else {
            buttons.put(Config.CUSTOMIZE_TOGGLE_ADMITTED_BUTTON_SLOT, new DisabledFeatureButton());
        }

        return buttons;
    }

    @Override
    public void close(Player player) {
        Menu.getMenus().remove(player.getUniqueId());
        if (sanctionVariantManager.getCurrentIndex().get(player) != null) {
            sanctionVariantManager.getCurrentIndex().remove(player);
        }

        if (sanctionVariantManager.getSelected().get(player) != null) {
            sanctionVariantManager.getSelected().remove(player);
        }

        if (sanctionsManager.getAdmittedToggles().contains(player) && !sanctionsManager.isEditingMode(player)) {
            sanctionsManager.removeAdmittedToggle(player);
        }
    }
}
