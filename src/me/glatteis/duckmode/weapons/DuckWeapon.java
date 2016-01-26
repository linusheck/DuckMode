package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;


public abstract class DuckWeapon implements Listener {

    private Material weaponMaterial;

    protected DuckWeapon(Material weaponMaterial) {
        this.weaponMaterial = weaponMaterial;
    }

    public Material getWeaponMaterial() {
        return weaponMaterial;
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
        meta.setLore(Collections.singletonList(UUID.randomUUID().toString()));
        meta.setDisplayName(" ");
        i.setItemMeta(meta);
        return i;
    }

    @EventHandler
    public void shootEvent(PlayerInteractEvent event) {
        if (event.getMaterial() != null && event.getMaterial().equals(weaponMaterial) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            shoot(event);
        }
    }

    public void shoot(PlayerInteractEvent event) {
    }

}