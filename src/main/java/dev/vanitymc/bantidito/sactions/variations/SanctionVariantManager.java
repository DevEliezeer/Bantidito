package dev.vanitymc.bantidito.sactions.variations;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SanctionVariantManager {

    private final Map<Player, SanctionVariant> selected;
    private Map<Player, Integer> currentIndex;

    public SanctionVariantManager() {
        this.selected = new HashMap<>();
        this.currentIndex = new HashMap<>();
    }

    public boolean hasSelected(Player player) {
        return selected.containsKey(player);
    }

    public boolean hasSelected(Player player, SanctionVariant variant) {
        return selected.containsKey(player) && selected.get(player) == variant;
    }
}
