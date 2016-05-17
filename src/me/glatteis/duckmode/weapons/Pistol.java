package me.glatteis.duckmode.weapons;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Pistol extends DuckPistol {

    public Pistol() {
        super(Material.BLAZE_POWDER, 6, 15);
    }

    @Override
    public void safeShoot(final PlayerInteractEvent e) {
        Arrow a = e.getPlayer().launchProjectile(Arrow.class, e.getPlayer().getLocation().getDirection());
        a.setShooter(e.getPlayer());
        a.setVelocity(a.getVelocity().multiply(4));
        a.setCustomName("Pistol");
    }

    @EventHandler
    public void onArrowImpact(ProjectileHitEvent e) {
        handleArrowImpact("Pistol", e);
    }

}
