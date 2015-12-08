package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class LUKFUHead extends Hat {

    private static ItemStack lukfu = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

    public LUKFUHead() {
        super(lukfu, Messages.getString("LUKFUHead.head"), Messages.getString("LUKFUHead.description")); //$NON-NLS-1$ //$NON-NLS-2$
        SkullMeta meta = (SkullMeta) lukfu.getItemMeta();
        meta.setOwner("Lukfu"); //$NON-NLS-1$
        lukfu.setItemMeta(meta);
    }

}
