package me.glatteis.duckmode.weapons;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RocketLauncher extends DuckGun {


    public RocketLauncher() {
        super(Material.DIAMOND, 6, 60);
    }

    @Override
    public void safeShoot(PlayerInteractEvent e) {
        Snowball s = e.getPlayer().launchProjectile(Snowball.class, e.getPlayer().getLocation().getDirection());
        s.setCustomName("RocketLauncher");
        s.setVelocity(s.getVelocity().multiply(4));
        s.setShooter(e.getPlayer());
    }


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if ((!e.getEntity().getType().equals(EntityType.SNOWBALL)) ||
                e.getEntity().getCustomName() == null ||
                (!e.getEntity().getCustomName().equals("RocketLauncher"))) return;
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 4);
    }


}
