package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RocketLauncher extends DuckWeapon {


    public RocketLauncher() {
        super(Material.DIAMOND);
    }

    @Override
    public void shoot(final PlayerInteractEvent e) {
        if (!WeaponWatch.durability.containsKey(e.getItem().getItemMeta().getLore())) {
            WeaponWatch.durability.put(e.getItem().getItemMeta().getLore(), 6);
        }
        if (WeaponWatch.durability.get(e.getItem().getItemMeta().getLore()) <= 0 ||
                WeaponWatch.cooldown.contains(e.getItem().getItemMeta().getLore())) return;
        WeaponWatch.durability.put(e.getItem().getItemMeta().getLore(), WeaponWatch.durability.get(e.getItem().getItemMeta().getLore()) - 1);

        Snowball s = e.getPlayer().launchProjectile(Snowball.class);
        s.setCustomName("RocketLauncher");
        s.setVelocity(s.getVelocity().multiply(4));
        s.setShooter(e.getPlayer());

        WeaponWatch.cooldown.add(e.getItem().getItemMeta().getLore());
        new BukkitRunnable() {
            public void run() {
                WeaponWatch.cooldown.remove(e.getItem().getItemMeta().getLore());
            }
        }.runTaskLater(DuckMain.getPlugin(), 100L);
    }


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if ((!e.getEntity().getType().equals(EntityType.SNOWBALL)) ||
                e.getEntity().getCustomName() == null ||
                (!e.getEntity().getCustomName().equals("RocketLauncher"))) return;
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 4);
    }


}
