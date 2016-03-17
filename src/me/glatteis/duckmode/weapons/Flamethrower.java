package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by Linus on 22.01.2016.
 */
public class Flamethrower extends DuckWeapon {

    public Flamethrower() {
        super(Material.BOOK);
    }

    @Override
    public void shoot(final PlayerInteractEvent event) {
        List<String> itemLore = event.getItem().getItemMeta().getLore();

        if (WeaponWatch.durability.get(itemLore) != null && WeaponWatch.durability.get(itemLore) == 0) return;
        if (WeaponWatch.cooldown.contains(itemLore)) return;

        if (WeaponWatch.durability.get(itemLore) == null) WeaponWatch.durability.put(itemLore, 6);
        else WeaponWatch.durability.put(itemLore, WeaponWatch.durability.get(itemLore) - 1);

        Vector direction = event.getPlayer().getLocation().getDirection();
        Vector location = event.getPlayer().getLocation().toVector();
        location.add(direction);

        Block floor = event.getPlayer().getLocation().getBlock();
        int count = 4;
        while (floor.getType().equals(Material.AIR) && count >= 0) {
            floor = floor.getRelative(BlockFace.DOWN);
            count--;
        }

        int floorY = floor.getY();

        for (int i = 0; i < 6; i++) {
            location.add(direction);
            Block b = event.getPlayer().getWorld().getBlockAt(location.getBlockX(), floorY, location.getBlockZ());
            if (b.getType() == Material.AIR) continue;
            b.getRelative(BlockFace.UP).setType(Material.FIRE);
        }

        Fireball b = event.getPlayer().getWorld().spawn(location.toLocation(DuckMain.getWorld(),
                event.getPlayer().getLocation().getYaw(), event.getPlayer().getLocation().getPitch()), Fireball.class);
        b.setVelocity(event.getPlayer().getLocation().getDirection().multiply(4));
        b.setShooter(event.getPlayer());

        DuckMain.getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 0);

        WeaponWatch.cooldown.add(event.getItem().getItemMeta().getLore());

        new BukkitRunnable() {
            @Override
            public void run() {
                WeaponWatch.cooldown.remove(event.getItem().getItemMeta().getLore());
            }
        }.runTaskLater(DuckMain.getPlugin(), 200);

    }

}
