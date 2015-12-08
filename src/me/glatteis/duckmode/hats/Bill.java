package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Bill extends Hat {

    public Bill() {
        super(new ItemStack(Material.DIAMOND_ORE), Messages.getString("Bill.head"), Messages.getString("Bill.description")); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
