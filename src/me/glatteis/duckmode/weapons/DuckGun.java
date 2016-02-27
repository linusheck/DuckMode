package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Linus on 27.02.2016.
 */
public abstract class DuckGun extends DuckWeapon {

    /*
    Represents a gun. This is a classic weapon with a cooldown and a delay between shots.
     */

    private int ammo;
    private long delay;

    protected DuckGun(Material weaponMaterial, int ammo, long delay) {
        super(weaponMaterial);
        this.ammo = ammo;
        this.delay = delay;
    }

    @Override
    public void shoot(final PlayerInteractEvent e) {
        if (!WeaponWatch.durability.containsKey(e.getItem().getItemMeta().getLore())) {
            WeaponWatch.durability.put(e.getItem().getItemMeta().getLore(), ammo);
        }
        if (WeaponWatch.durability.get(e.getItem().getItemMeta().getLore()) > 0 &&
                (!WeaponWatch.cooldown.contains(e.getItem().getItemMeta().getLore()))) {
            WeaponWatch.durability.put(e.getItem().getItemMeta().getLore(), WeaponWatch.durability.get(e.getItem().getItemMeta().getLore()) - 1);
            WeaponWatch.cooldown.add(e.getItem().getItemMeta().getLore());
            safeShoot(e);
            new BukkitRunnable() {
                @Override
                public void run() {
                    WeaponWatch.cooldown.remove(e.getItem().getItemMeta().getLore());
                }
            }.runTaskLater(DuckMain.getPlugin(), delay);
        }
    }

    //New method to override
    public abstract void safeShoot(PlayerInteractEvent event);
}
