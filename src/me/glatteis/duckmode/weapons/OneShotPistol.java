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

public class OneShotPistol extends DuckGun {

    public OneShotPistol() {
        super(Material.BLAZE_ROD, 1, 0);
    }

    @Override
    public void safeShoot(final PlayerInteractEvent e) {
        DuckMain.getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 1);
        Arrow a = e.getPlayer().launchProjectile(Arrow.class, e.getPlayer().getLocation().getDirection());
        a.setShooter(e.getPlayer());
        a.setVelocity(a.getVelocity().multiply(4));
        a.setCustomName("OneShotPistol");
    }

    @EventHandler
    public void onArrowImpact(ProjectileHitEvent e) {
        if (e.getEntity().getCustomName() == null) return;
        if (e.getEntity().getCustomName().equals("OneShotPistol")) {
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
