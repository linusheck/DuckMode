package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Linus on 22.01.2016.
 */
public class MachineGun extends DuckWeapon {

    private HashMap<List<String>, Integer> shotsLeft = new HashMap<List<String>, Integer>();

    public MachineGun() {
        super(Material.WHEAT);
    }

    @Override
    public void shoot(PlayerInteractEvent event) {
        if (event.getMaterial() == null) return;
        final List<String> itemLore = event.getItem().getItemMeta().getLore();

        if (WeaponWatch.durability.get(itemLore) == null) WeaponWatch.durability.put(itemLore, 4);
        if (WeaponWatch.durability.get(itemLore) == 0) return;
        if (WeaponWatch.cooldown.contains(itemLore)) return;

        if (shotsLeft.get(itemLore) != null && shotsLeft.get(itemLore) == 0) {
            WeaponWatch.cooldown.add(itemLore);
            shotsLeft.remove(itemLore);
            new BukkitRunnable() {
                @Override
                public void run() {
                    WeaponWatch.durability.put(itemLore, WeaponWatch.durability.get(itemLore) - 1);
                    WeaponWatch.cooldown.remove(itemLore);
                }
            }.runTaskLater(DuckMain.plugin, 80);
        }

        int shotsPerRound = 6; //@Balancing: Specify shots per round here

        if (shotsLeft.get(itemLore) == null)
            shotsLeft.put(itemLore, shotsPerRound);
        else
            shotsLeft.put(itemLore, shotsLeft.get(itemLore) - 1);

        DuckMain.getWorld().playSound(event.getPlayer().getLocation(), Sound.BLAZE_HIT, 10, 1);
        Arrow a = event.getPlayer().launchProjectile(Arrow.class);
        a.setShooter(event.getPlayer());
        a.setVelocity(a.getVelocity().multiply(6));
        a.setCustomName("MachineGun");


    }


}
