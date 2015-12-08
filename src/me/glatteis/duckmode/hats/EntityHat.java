package me.glatteis.duckmode.hats;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class EntityHat extends Hat {

    protected Entity[] entities;
    protected ItemStack showItemStack;

    protected EntityHat(String name, String description, ItemStack showItemStack) {
        super(new ItemStack(Material.DIAMOND_ORE), name, description);
        this.showItemStack = showItemStack;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public ItemStack getShowItemStack() {
        return showItemStack;
    }


}
