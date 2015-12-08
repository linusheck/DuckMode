package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ExplosionHandler implements Listener {

    List<ItemStack> dropStacks = new ArrayList<ItemStack>();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void saveExplosions(BlockExplodeEvent e) {
        final ArrayList<BlockState> explodedBlocks = new ArrayList<BlockState>();
        for (Block b : e.blockList()) {
            explodedBlocks.add(b.getState());
            dropStacks.addAll(b.getDrops());
            b.setType(Material.AIR);
        }
        new BukkitRunnable() {
            public void run() {
                for (BlockState b : explodedBlocks) {
                    if (b.getType().equals(Material.FIRE)) continue;
                    FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getRawData());
                    fallingBlock.setDropItem(false);
                    fallingBlock.setVelocity(new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1, Math.random() * 2 - 1));
                }
                for (Entity e : DuckMain.getWorld().getEntities()) {
                    if (e.getType().equals(EntityType.DROPPED_ITEM)) {
                        Item i = (Item) e;
                        if (dropStacks.contains(i.getItemStack())) {
                            e.remove();
                        }
                    }
                }
            }
        }.runTaskLater(DuckMain.getPlugin(), 6L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void saveExplosions(EntityExplodeEvent e) {
        final ArrayList<BlockState> explodedBlocks = new ArrayList<BlockState>();
        for (Block b : e.blockList()) {
            explodedBlocks.add(b.getState());
            dropStacks.addAll(b.getDrops());
            b.setType(Material.AIR);
        }
        new BukkitRunnable() {
            public void run() {
                for (BlockState b : explodedBlocks) {
                    if (b.getType().equals(Material.FIRE)) continue;
                    FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getRawData());
                    fallingBlock.setDropItem(false);
                    fallingBlock.setVelocity(new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1, Math.random() * 2 - 1));
                }
                for (Entity e : DuckMain.getWorld().getEntities()) {
                    if (e.getType().equals(EntityType.DROPPED_ITEM)) {
                        Item i = (Item) e;
                        if (dropStacks.contains(i.getItemStack())) {
                            e.remove();
                        }
                    }
                }
            }
        }.runTaskLater(DuckMain.getPlugin(), 6L);
    }


}
