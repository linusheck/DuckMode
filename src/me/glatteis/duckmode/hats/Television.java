package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Television extends Hat {

    public Television() {
        super(new ItemStack(Material.REDSTONE_ORE), Messages.getString("Television.head"), Messages.getString("Television.description"));
    }

}
