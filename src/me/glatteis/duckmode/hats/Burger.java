package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Burger extends Hat {

    public Burger() {
        super(new ItemStack(Material.IRON_ORE), Messages.getString("Burger.head"), Messages.getString("Burger.description"));
    }

}
