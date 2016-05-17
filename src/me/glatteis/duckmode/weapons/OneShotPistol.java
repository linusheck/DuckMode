package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class OneShotPistol extends DuckPistol {

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
        handleArrowImpact("OneShotPistol", e);
    }

}
