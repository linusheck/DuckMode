package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class EisHead extends Hat {

    private static ItemStack eis = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

    public EisHead() {
        super(eis, Messages.getString("EisHead.head"), Messages.getString("EisHead.description")); //$NON-NLS-1$ //$NON-NLS-2$
        SkullMeta meta = (SkullMeta) eis.getItemMeta();
        meta.setOwner("eis"); //$NON-NLS-1$
        eis.setItemMeta(meta);
    }

}
