package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Crocodile extends Hat {

    public Crocodile() {
        super(new ItemStack(Material.LAPIS_ORE), Messages.getString("Crocodile.head"), Messages.getString("Crocodile.description"));
    }

}
