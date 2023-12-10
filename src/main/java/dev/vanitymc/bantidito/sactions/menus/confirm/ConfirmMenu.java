package dev.vanitymc.bantidito.sactions.menus.confirm;

import dev.vanitymc.bantidito.config.Config;
import dev.vanitymc.bantidito.sactions.SanctionCustom;
import dev.vanitymc.bantidito.sactions.SanctionsManager;
import dev.vanitymc.bantidito.sactions.menus.confirm.buttons.ConfirmButton;
import dev.vanitymc.bantidito.utilities.menu.Button;
import dev.vanitymc.bantidito.utilities.menu.Menu;
import dev.vanitymc.bantidito.utilities.menu.buttons.RefillButton;
import dev.vanitymc.bantidito.sactions.menus.confirm.buttons.DeclineButton;
import dev.vanitymc.bantidito.sactions.menus.confirm.buttons.InfoButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConfirmMenu extends Menu {

    private final SanctionsManager sanctionsManager;

    private final String target;
    private final SanctionCustom sanctionCustom;

    @Override
    public String getTitle(Player player) {
        return Config.CONFIRM_TITLE.replace("<target>", target);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int confirmButton : Config.CONFIRM_BUTTON_SLOTS) {
            buttons.put(confirmButton, new ConfirmButton(target, sanctionCustom));
        }

        if (Config.CONFIRM_REFILL_ENABLED) {
            for (int refill : Config.CONFIRM_REFILL_SLOTS) {
                buttons.put(refill, new RefillButton(Config.CONFIRM_REFILL_DATA));
            }
        }

        buttons.put(Config.CONFIRM_SANCTION_INFO_SLOT, new InfoButton(sanctionsManager, target, sanctionCustom));

        for (int declineButton : Config.DECLINE_BUTTON_SLOTS) {
            buttons.put(declineButton, new DeclineButton(sanctionsManager, target));
        }

        return buttons;
    }
}
