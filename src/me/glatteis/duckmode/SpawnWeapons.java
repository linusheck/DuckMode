package me.glatteis.duckmode;

import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SpawnWeapons {

    public static final List<Location> locations = new ArrayList<Location>();

    public static void enable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location l : locations) {
                    boolean spawnThere = true;
                    for (Entity e : DuckMain.getWorld().getEntities()) {
                        if (e instanceof Item && e.getLocation().distanceSquared(l) < 3) {
                            spawnThere = false;
                        }
                    }
                    if (!spawnThere) {
                        continue;
                    }
                    WeaponWatch.spawnRandomWeapon(l);
                }
            }
        }.runTaskTimer(DuckMain.getPlugin(), 20L, 400L);
    }


}
