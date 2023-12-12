package dev.styles.bantidito.sactions.menus.sanction;

import dev.styles.bantidito.config.Config;
import dev.styles.bantidito.sactions.SanctionPage;
import dev.styles.bantidito.sactions.SanctionsManager;
import dev.styles.bantidito.sactions.menus.sanction.buttons.InfoButton;
import dev.styles.bantidito.sactions.menus.sanction.buttons.pagination.NextPageButton;
import dev.styles.bantidito.sactions.menus.sanction.buttons.pagination.PreviousPageButton;
import dev.styles.bantidito.utilities.menu.Button;
import dev.styles.bantidito.utilities.menu.Menu;
import dev.styles.bantidito.utilities.menu.buttons.RefillButton;
import dev.styles.bantidito.sactions.Sanction;
import dev.styles.bantidito.sactions.menus.sanction.buttons.SanctionButton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class SanctionsMenu extends Menu {

    private final SanctionPage sanctionPage;

    private final String target;
    private final SanctionsManager sanctionsManager;

    @Override
    public String getTitle(Player player) {
        return Config.SANCTIONS_TITLE.replace("<target>", target);
    }

    @Override
    public int getSize() {
        return Config.SANCTIONS_ROWS * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (Config.SANCTIONS_REFILL_ENABLED) {
            for (int refill : Config.SANCTIONS_REFILL_SLOTS) {
                buttons.put(refill, new RefillButton(Config.SANCTIONS_REFILL_DATA));
            }
        }

        buttons.put(Config.SANCTIONS_PLAYER_INFO_SLOT, new InfoButton(target));

        if (Config.SANCTIONS_PREVIOUS_PAGE_BUTTON_ENABLED) {
            buttons.put(Config.SANCTIONS_PREVIOUS_PAGE_BUTTON_SLOT, new PreviousPageButton(this));
        }

        if (Config.SANCTIONS_NEXT_PAGE_BUTTON_ENABLED) {
            buttons.put(Config.SANCTIONS_NEXT_PAGE_BUTTON_SLOT, new NextPageButton(this));
        }

        for (Sanction sanction : sanctionPage.getSanctions()) {
            buttons.put(sanction.getSlot(), new SanctionButton(sanctionsManager, target, sanction));
        }

        return buttons;
    }
}
