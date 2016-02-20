package me.glatteis.duckmode.listener;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.game.GameState;
import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class PlayerGameListener implements Listener {

    List<Material> meleeWeapons = Collections.singletonList(Material.IRON_SWORD);



    public static void explosion(Location location, List<Block> blockList) {
        double radius = 0;
        double helper;
        for (Block b : blockList) {
            if ((helper = b.getLocation().distance(location)) > radius) {
                radius = helper;
            }
        }
        for (Duck duck : DuckMain.ducks) {
            if (duck.getPlayer().getLocation().distance(location) < radius) {
                duck.die(Messages.getString("you_blew_up"));
            }
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent e) {
        e.setCancelled(true);
        String killCause = "";
        if (e.getCause().equals(DamageCause.FALL) || e.getCause().equals(DamageCause.SUFFOCATION)) {
            return;
        }
        if (!(e.getEntity() instanceof Player) && e.getCause().equals(DamageCause.VOID)) {
            e.getEntity().remove();
            return;
        }
        if (!DuckMain.state.equals(GameState.INGAME)) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            Bukkit.getLogger().info(e.getCause().toString());
            if (e instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) e;
                Entity damager = edbeEvent.getDamager();
                if (damager instanceof Player) {
                    if (!meleeWeapons.contains(((Player) damager).getItemInHand().getType())) {
                        return;
                    }
                    killCause = Messages.getString("you_were_slain_by") + damager.getName() + ".";
                } else if (damager instanceof Arrow) {
                    Arrow a = (Arrow) damager;
                    if (a.getShooter() != null && a.getShooter() instanceof Player) {
                        killCause = Messages.getString("you_were_shot_by") + ((Player) a.getShooter()).getName() + ".";
                    }
                }
            }
            if (e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
                killCause = Messages.getString("you_blew_up");
            }
            if (e.getCause().equals(DamageCause.VOID)) {
                killCause = Messages.getString("you_fell_out_of_world");
            }
            if (e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK) || e.getCause().equals(DamageCause.LAVA)) {
                for (final Duck d : DuckMain.ducks) {
                    if (d.getPlayer().equals(e.getEntity())) {
                        new BukkitRunnable() {
                            public void run() {
                                if (d.getPlayer().getFireTicks() > 0)
                                    d.die(Messages.getString("you_became_christmas_duck"));
                            }
                        }.runTaskLater(DuckMain.getPlugin(), 100L);
                        return;
                    }
                }
            }
            for (Duck d : DuckMain.ducks) {
                if (d.getPlayer().equals(e.getEntity())) {
                    d.die(killCause);
                }
            }
        }
    }


}
