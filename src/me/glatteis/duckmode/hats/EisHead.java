package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class EisHead extends Hat {

    private static ItemStack eis = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

    public EisHead() {
        super(eis, Messages.getString("EisHead.head"), Messages.getString("EisHead.description"));
        SkullMeta meta = (SkullMeta) eis.getItemMeta();
        meta.setOwner("eis");
        eis.setItemMeta(meta);
    }

}
