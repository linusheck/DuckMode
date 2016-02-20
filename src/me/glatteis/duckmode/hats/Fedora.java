package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Fedora extends Hat {

    public Fedora() {
        super(new ItemStack(Material.COAL_ORE), Messages.getString("Fedora.head"), Messages.getString("Fedora.description"));
    }

}
