package me.glatteis.duckmode.hats;

import me.glatteis.duckmode.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Pumpkin extends Hat {

    public Pumpkin() {
        super(new ItemStack(Material.JACK_O_LANTERN), Messages.getString("Pumpkin.head"), Messages.getString("Pumpkin.description"));
    }

}