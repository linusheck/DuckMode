package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Created by Linus on 25.02.2016.
 */
public class IceRay extends DuckGun {

    public IceRay() {
        super(Material.STRING, 6, 40);
    }

    @Override
    public void safeShoot(PlayerInteractEvent event) {
        Vector velocity = event.getPlayer().getLocation().getDirection().multiply(2.5);
        Location location = event.getPlayer().getLocation();
        IceRayBullet bullet = new IceRayBullet(velocity, location, event.getPlayer());
        bullet.getCalcRunnable().runTaskTimer(DuckMain.getPlugin(), 0, 0);
        DuckMain.getWorld().playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1.2f, 1);
    }

    private void freezePlayer(final Duck duck) {
        final Player player = duck.getPlayer();
        int delay = 60;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, delay));
        player.getInventory().setHelmet(new ItemStack(Material.ICE));
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().setHelmet(duck.getHat().getStack());
            }
        }.runTaskLater(DuckMain.getPlugin(), 60);
    }

    private class IceRayBullet {
        private BukkitRunnable calcRunnable;

        private IceRayBullet(final Vector velocity, final Location location, final Player shooter) {
            calcRunnable =
                    new BukkitRunnable() {
                        private int totalDistance = 0;

                        public void run() {
                            location.add(velocity);
                            totalDistance += 1;
                            if (location.getBlock().getType().isSolid()) {
                                cancel();
                                return;
                            }
                            for (Duck duck : DuckMain.getPlugin().getDucks()) {
                                if (duck.getPlayer().getLocation().distanceSquared(location) < 0.5 &&
                                        !duck.getPlayer().equals(shooter)) {
                                    freezePlayer(duck);
                                    cancel();
                                    return;
                                }
                            }
                            for (int i = 0; i < 10; i++)
                                DuckMain.getWorld().spigot().playEffect(location, Effect.CLOUD);
                            if (totalDistance > 10) cancel();
                        }
                    };
        }

        private BukkitRunnable getCalcRunnable() {
            return calcRunnable;
        }
    }
}
