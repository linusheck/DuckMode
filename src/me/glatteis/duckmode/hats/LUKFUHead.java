package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class LUKFUHead extends Hat {

    private static ItemStack lukfu = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

    public LUKFUHead() {
        super(lukfu, Messages.getString("LUKFUHead.head"), Messages.getString("LUKFUHead.description"));
        SkullMeta meta = (SkullMeta) lukfu.getItemMeta();
        meta.setOwner("Lukfu");
        lukfu.setItemMeta(meta);
    }

}
