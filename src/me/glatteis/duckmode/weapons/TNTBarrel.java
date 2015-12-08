package me.glatteis.duckmode.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

public class TNTBarrel extends DuckWeapon implements Listener {

    public TNTBarrel() {
        super(null);
    }

    @Override
    public void spawnWeapon(Location l) {
        l.getBlock().setType(Material.TNT);
    }

    @EventHandler
    public void explode(ProjectileHitEvent e) {
        BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
        Block hitBlock = null;
        while (iterator.hasNext()) {
            hitBlock = iterator.next();
            if (!hitBlock.getType().equals(Material.AIR)) {
                break;
            }
        }
        if (hitBlock != null && hitBlock.getType().equals(Material.TNT)) {
            Location l = hitBlock.getLocation().clone();
            for (int x = -3; x < 4; x++) {
                for (int z = -3; z < 4; z++) {
                    if (x == 0 && z == 0) continue;
                    Block b = l.clone().add(x, -1, z).getBlock();
                    if (!b.getType().equals(Material.AIR)) {
                        double ran = Math.random();
                        if (ran < 0.3) {
                            l.clone().add(x, 0, z).getBlock().setType(Material.FIRE);
                        }
                    }
                }
            }
        }
    }
}