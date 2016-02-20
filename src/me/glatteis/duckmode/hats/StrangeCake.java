package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StrangeCake extends Hat {

    public StrangeCake() {
        super(new ItemStack(Material.LAPIS_BLOCK), Messages.getString("StrangeCake.head"), Messages.getString("StrangeCake.description"));
    }

}
