package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Linus on 24.01.2016.
 */
public class SuicideBomb extends DuckWeapon {

    public SuicideBomb() {
        super(Material.BOWL);
    }

    @Override
    public void shoot(final PlayerInteractEvent event) {
        DuckMain.getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 0);
        event.getPlayer().getInventory().remove(Material.BOWL);
        new BukkitRunnable() {
            int i = 0;

            public void run() {
                DuckMain.getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, 1, 1);
                if (i == 5) {
                    DuckMain.getWorld().createExplosion(event.getPlayer().getLocation(), 20);
                    cancel();
                    return;
                }
                i++;
            }
        }.runTaskTimer(DuckMain.getPlugin(), 5, 5);
    }


}
