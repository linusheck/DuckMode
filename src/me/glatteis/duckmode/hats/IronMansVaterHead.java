package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class IronMansVaterHead extends Hat {

    private static ItemStack imv = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

    public IronMansVaterHead() {
        super(imv, Messages.getString("IronMansVaterHead.head"), Messages.getString("IronMansVaterHead.description")); //$NON-NLS-1$ //$NON-NLS-2$
        SkullMeta meta = (SkullMeta) imv.getItemMeta();
        meta.setOwner("IronMansVater"); //$NON-NLS-1$
        imv.setItemMeta(meta);
    }

}
