package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Crocodile extends Hat {

    public Crocodile() {
        super(new ItemStack(Material.LAPIS_ORE), Messages.getString("Crocodile.head"), Messages.getString("Crocodile.description")); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
