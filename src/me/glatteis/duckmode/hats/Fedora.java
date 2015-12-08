package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Fedora extends Hat {

    public Fedora() {
        super(new ItemStack(Material.COAL_ORE), Messages.getString("Fedora.head"), Messages.getString("Fedora.description")); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
