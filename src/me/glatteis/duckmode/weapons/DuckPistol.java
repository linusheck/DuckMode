package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;

/**
 * Created by Linus on 17.05.2016.
 */
public abstract class DuckPistol extends DuckGun {

    protected DuckPistol(Material weaponMaterial, int ammo, long delay) {
        super(weaponMaterial, ammo, delay);
    }

    public void handleArrowImpact(String name, ProjectileHitEvent e) {
        if (e.getEntity().getCustomName() == null) return;
        if (e.getEntity().getCustomName().equals(name)) {
            BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
            Block hitBlock = null;
            while (iterator.hasNext()) {
                hitBlock = iterator.next();
                if (!hitBlock.getType().equals(Material.AIR)) {
                    break;
                }
            }
            if (hitBlock == null) return;
            if (hitBlock.getType().equals(Material.GLASS) || hitBlock.getType().equals(Material.STAINED_GLASS)) {
                hitBlock.setType(Material.AIR);
                for (Duck d : DuckMain.getPlugin().getDucks()) {
                    d.getPlayer().playSound(hitBlock.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);
                }
            }
        }
    }

    @Override
    public abstract void safeShoot(PlayerInteractEvent event);

}
