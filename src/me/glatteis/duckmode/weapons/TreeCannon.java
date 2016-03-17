package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Linus on 22.01.2016.
 */
public class TreeCannon extends DuckGun {

    public TreeCannon() {
        super(Material.SAPLING, 10, 100);
    }

    @Override
    public void safeShoot(final PlayerInteractEvent event) {
        DuckMain.getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_HORSE_AMBIENT, 10, 1);
        Arrow a = event.getPlayer().launchProjectile(Arrow.class, event.getPlayer().getLocation().getDirection());
        a.setShooter(event.getPlayer());
        a.setVelocity(a.getVelocity().multiply(2));
        a.setCustomName("TreeCannon");
    }

    @EventHandler
    public void onArrowImpact(ProjectileHitEvent e) {
        if (e.getEntity().getCustomName() == null) return;
        if (e.getEntity().getCustomName().equals("TreeCannon")) {
            Block b = e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);
            boolean treeGrown = false;
            int i = 0;
            while (!treeGrown && i < 5) {
                b.getRelative(BlockFace.DOWN).setType(Material.AIR);
                b.setType(Material.DIRT);
                b = b.getRelative(BlockFace.UP);
                treeGrown = DuckMain.getWorld().generateTree(b.getLocation(), TreeType.TREE);
                i++;
            }
        }
    }

}
