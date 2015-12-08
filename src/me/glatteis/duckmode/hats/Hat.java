package me.glatteis.duckmode.hats;

import org.bukkit.inventory.ItemStack;

public class Hat {

    protected String lore;
    protected ItemStack item;
    protected String name;

    protected Hat(ItemStack m, String name, String lore) {
        item = m;
        this.name = name;
        this.lore = lore;
    }

    public String getLore() {
        return lore;
    }

    public ItemStack getStack() {
        return item;
    }

    public String getName() {
        return name;
    }

}
