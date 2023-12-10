package dev.vanitymc.bantidito.sactions;

import dev.vanitymc.bantidito.sactions.enums.SanctionType;
import dev.vanitymc.bantidito.sactions.variations.SanctionVariant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
@RequiredArgsConstructor
public class Sanction {

    private final int slot;
    private final ItemStack icon;
    private final String name;
    private final SanctionType type;
    private final String reason;
    private final String duration;
    private final ArrayList<SanctionVariant> variations;
    private final boolean admittedSettings;
}
