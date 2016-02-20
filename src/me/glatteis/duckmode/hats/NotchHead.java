package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class NotchHead extends Hat {

    private static ItemStack notch = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

    public NotchHead() {
        super(notch, Messages.getString("NotchHead.head"), Messages.getString("NotchHead.description"));
        SkullMeta meta = (SkullMeta) notch.getItemMeta();
        meta.setOwner("Notch");
        notch.setItemMeta(meta);
    }

}
