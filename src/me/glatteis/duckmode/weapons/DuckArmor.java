package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.Duck;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DuckArmor extends DuckWeapon {

    List<Material> helmets = Arrays.asList();
    List<Material> chestplates = Arrays.asList(Material.IRON_CHESTPLATE);
    List<Material> leggings = Arrays.asList(Material.IRON_LEGGINGS);
    List<Material> everything;

    public DuckArmor() {
        super(null);
        everything = new ArrayList<Material>();
        everything.addAll(helmets);
        everything.addAll(chestplates);
        everything.addAll(leggings);
    }

    public static boolean willDie(Duck d) {
        Player p = d.getPlayer();
        if (p.getInventory().getChestplate() != null) {
            double chestRandom = Math.random();
            if (chestRandom < 0.7) {
                p.getInventory().setChestplate(null);
                p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 10, 1);
                return false;
            }

        }
        if (p.getInventory().getLeggings() != null) {
            double legRandom = Math.random();
            if (legRandom < 0.7) {
                p.getInventory().setLeggings(null);
                p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 10, 1);
                return false;
            }

        }
        return true;
    }

    @Override
    public void spawnWeapon(Location l) {
        Random r = new Random();
        int ran = r.nextInt(everything.size());
        l.getWorld().dropItemNaturally(l, initializeItemStack(new ItemStack(everything.get(ran))));
    }

    @EventHandler
    public void onArmorEquip(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        e.setCancelled(true);
        Player p = e.getPlayer();
        if (helmets.contains(e.getItem().getType())) {
            if (p.getInventory().getHelmet() != null) {
                p.getLocation().getWorld().dropItem(p.getLocation().add(0, 2, 0), p.getInventory().getHelmet());
            }
            p.getInventory().setHelmet(e.getItem());

            p.getInventory().setItem(4, null);
            p.updateInventory();
            return;
        } else if (chestplates.contains(e.getItem().getType())) {
            if (p.getInventory().getChestplate() != null) {
                p.getLocation().getWorld().dropItem(p.getLocation().add(0, 2, 0), p.getInventory().getChestplate());
            }
            p.getInventory().setChestplate(e.getItem());

            p.getInventory().setItem(4, null);
            p.updateInventory();
            return;
        } else if (leggings.contains(e.getItem().getType())) {
            if (p.getInventory().getLeggings() != null) {
                p.getLocation().getWorld().dropItem(p.getLocation().add(0, 2, 0), p.getInventory().getLeggings());
            }
            p.getInventory().setLeggings(e.getItem());

            p.getInventory().setItem(4, null);
            p.updateInventory();
            return;
        }
        e.setCancelled(false);
    }

}
