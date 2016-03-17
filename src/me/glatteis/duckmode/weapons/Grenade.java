package me.glatteis.duckmode.weapons;

import me.glatteis.duckmode.DuckMain;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grenade extends DuckWeapon {

    HashMap<List<String>, Item> thrownGrenades = new HashMap<List<String>, Item>();
    List<List<String>> fusedGrenades = new ArrayList<List<String>>();

    public Grenade() {
        super(Material.POTATO_ITEM);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.POTATO_ITEM) || e.getItemDrop().getItemStack().getType().equals(Material.BAKED_POTATO)) {
            throwGrenade(e.getItemDrop().getItemStack(), e.getPlayer(), e);
        }
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent e) {
        if (e.getItem() == null || (!e.getItem().getType().equals(Material.POTATO_ITEM) && !e.getItem().getType().equals(Material.BAKED_POTATO)))
            return;
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            fuseGrenade(e.getItem(), e.getPlayer());
        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            throwGrenade(e.getItem(), e.getPlayer(), null);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (thrownGrenades.containsKey(e.getItem().getItemStack().getItemMeta().getLore())) {
            thrownGrenades.remove(e.getItem().getItemStack().getItemMeta().getLore());
        }
    }

    private void fuseGrenade(final ItemStack i, final Player p) {
        if (fusedGrenades.contains(i.getItemMeta().getLore())) return;
        fusedGrenades.add(i.getItemMeta().getLore());
        i.setType(Material.BAKED_POTATO);
        for (Player p2 : DuckMain.getWorld().getPlayers()) {
            p2.playSound(p.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 10, 1);
        }

        new BukkitRunnable() {
            public void run() {
                if (thrownGrenades.containsKey(i.getItemMeta().getLore())) {
                    Item item = thrownGrenades.get(i.getItemMeta().getLore());
                    item.getWorld().createExplosion(item.getLocation(), 6);
                    thrownGrenades.remove(i.getItemMeta().getLore());
                    fusedGrenades.remove(i.getItemMeta().getLore());
                    item.remove();
                } else {
                    for (Player thisPlayer : p.getWorld().getPlayers()) {
                        if (thisPlayer.getInventory().getItem(4) != null &&
                                thisPlayer.getInventory().getItem(4).getItemMeta().getLore().equals(i.getItemMeta().getLore()) ||
                                thisPlayer.getInventory().getItemInOffHand() != null &&
                                thisPlayer.getInventory().getItemInOffHand().getItemMeta().getLore().equals(i.getItemMeta().getLore())) {
                            thisPlayer.getWorld().createExplosion(thisPlayer.getLocation(), 6);
                            thisPlayer.getInventory().remove(i);
                            fusedGrenades.remove(i.getItemMeta().getLore());
                        }
                    }
                }
            }
        }.runTaskLater(DuckMain.getPlugin(), 60L);
    }

    private void throwGrenade(ItemStack i, Player p, PlayerDropItemEvent event) {
        if (event == null) { //The player has thrown the Item using left click.
            Item item = p.getWorld().dropItemNaturally(p.getLocation(), i);
            item.setVelocity(p.getLocation().getDirection().multiply(0.8));
            thrownGrenades.put(i.getItemMeta().getLore(), item);
            p.getInventory().setItem(4, null);
            p.getInventory().setItemInOffHand(null);
        } else { //The player has thrown the Item using Q.
            thrownGrenades.put(i.getItemMeta().getLore(), event.getItemDrop());
        }
    }

}
