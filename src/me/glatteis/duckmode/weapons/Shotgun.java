package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class Shotgun extends DuckGun {

    public Shotgun() {
        super(Material.SLIME_BALL, 6, 60);
    }

    @Override
    public void safeShoot(final PlayerInteractEvent e) {
        DuckMain.getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 1);
        for (int i = 0; i < 5; i++) {
            Arrow a = e.getPlayer().launchProjectile(Arrow.class, e.getPlayer().getLocation().getDirection());
            a.setShooter(e.getPlayer());
            a.setVelocity(a.getVelocity().multiply(4));
            a.setVelocity(a.getVelocity().add(new Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5)));
            a.setCustomName("Shotgun");
        }
    }

    @EventHandler
    public void onArrowImpact(ProjectileHitEvent e) {
        if (e.getEntity().getCustomName() == null) return;
        if (e.getEntity().getCustomName().equals("Shotgun")) {
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
                for (Duck d : DuckMain.ducks) {
                    d.getPlayer().playSound(hitBlock.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);
                }
            }
        }
    }

}
