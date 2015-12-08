package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;


public class DuckWeapon {

    private Material weaponMaterial;

    public Material getWeaponMaterial() {
        return weaponMaterial;
    }

    protected DuckWeapon(Material weaponMaterial) {
        this.weaponMaterial = weaponMaterial;
    }

    public void spawnWeapon(Location l) {
        if (!l.clone().add(0, -1, 0).getBlock().getType().equals(Material.IRON_BLOCK)) return;
        if (!l.getBlock().getType().equals(Material.AIR)) {
            Collection<ItemStack> i = l.getBlock().getDrops();
            l.getBlock().breakNaturally();
            for (Entity en : DuckMain.getWorld().getEntities()) {
                if (en.getType().equals(EntityType.DROPPED_ITEM) && i.contains(((Item) en).getItemStack())) {
                    en.remove();
                }
            }
        }
        ItemStack i = new ItemStack(weaponMaterial);
        initializeItemStack(i);
        l.getWorld().dropItem(l, i);
    }

    protected ItemStack initializeItemStack(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        meta.setLore(Arrays.asList(UUID.randomUUID().toString()));
        meta.setDisplayName(" "); //$NON-NLS-1$
        i.setItemMeta(meta);
        return i;
    }

}