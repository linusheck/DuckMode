package me.glatteis.duckmode.listener;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.StaticMethods;
import me.glatteis.duckmode.game.GameState;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    /*
    This class listens for a lot of stuff. These are all the game mechanics that didn't get their own class, like
    MOTD, join handling etc.
     */

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        ChatColor stateColor = (DuckMain.state.equals(GameState.LOBBY) ? ChatColor.GREEN : ChatColor.RED);
        ChatColor fullColor = (DuckMain.ducks.size() < DuckMain.maxPlayerCount ? ChatColor.GREEN : ChatColor.RED);
        String pingString = ChatColor.YELLOW + "\\" + ChatColor.UNDERLINE + Messages.getString("duck_mode") + ChatColor.RESET + ChatColor.YELLOW +
                "/ " + fullColor + Bukkit.getServer().getOnlinePlayers().size() + " " + Messages.getString("players_online") + ChatColor.RESET + " || " +
                stateColor + ChatColor.BOLD + DuckMain.state + "\n" + ChatColor.GRAY + Messages.getString("motd_description");
        e.setMaxPlayers(DuckMain.maxPlayerCount);
        e.setMotd(pingString);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (DuckMain.continueGame.canNotMove) {
            if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
            if (e.getFrom().distance(e.getTo()) < 0.1) return;
            if (e.getFrom().getY() > e.getTo().getY())
                e.getPlayer().teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(),
                        e.getTo().getY(), e.getFrom().getZ()));
            else e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (DuckMain.state.equals(GameState.LOBBY) && (!(Bukkit.getServer().getOnlinePlayers().size() > DuckMain.maxPlayerCount))) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().getInventory().setHeldItemSlot(4);
            Duck d = new Duck(e.getPlayer(), DuckMain.spawnLocation);
            DuckMain.ducks.add(d);
            d.prepareInventory();
            StaticMethods.disableJumping(e.getPlayer());
            DuckReflectionMethods.title(e.getPlayer(), ChatColor.RED + Messages.getString("big_screen_title"), 5, 30, 5);
            DuckReflectionMethods.subtitle(e.getPlayer(), Messages.getString("version") + " " + DuckMain.getPlugin().getDescription().getVersion(), 5, 30, 5);

            e.getPlayer().teleport(DuckMain.spawnLocation);
            e.setJoinMessage("DUCK MODE -> " + ChatColor.YELLOW + Messages.getString("duck") + " " + e.getPlayer().getName() + " " + Messages.getString("join_message"));

            if (DuckMain.autoStart > 0 && Bukkit.getOnlinePlayers().size() >= DuckMain.autoStart) {
                ListenerActivator.lobbyCountdown();
            }
        } else {
            DuckMain.spectators.add(e.getPlayer());
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.setJoinMessage(null);
            e.getPlayer().sendMessage(Messages.getString("spectator.join"));
            if (DuckMain.state.equals(GameState.LOBBY)) {
                e.getPlayer().teleport(DuckMain.spawnLocation);
            } else {
                Vector averageLocation = new Vector();
                int aliveDucks = 0;
                for (Duck d : DuckMain.ducks) {
                    aliveDucks++;
                    if (!d.isDead()) {
                        averageLocation = averageLocation.add(d.getPlayer().getLocation().toVector());
                    }
                }
                averageLocation = averageLocation.divide(new Vector(aliveDucks, aliveDucks, aliveDucks));
                e.getPlayer().teleport(averageLocation.toLocation(DuckMain.getWorld()));
            }
        }

        String resourcePackLink = DuckMain.indevResourcePack ?
                "http://glatteis.bplaced.net/DuckMode/resource_pack_indev.zip" :
                "http://glatteis.bplaced.net/DuckMode/rp.php"; //This PHP script just counts the number of downloads and redirects to the actual download
        e.getPlayer().setResourcePack(resourcePackLink);

        //Old resource pack links:
        //https://www.dropbox.com/s/baxsqe7310dwyze/rp_dev.zip?dl=1
        //https://www.dropbox.com/s/z9wbr65n6csvzsq/rp.zip?dl=1
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        for (Duck d : DuckMain.ducks) {
            if (d.getPlayer().equals(e.getPlayer())) {
                if (!DuckMain.duckCount.isEmpty()) {
                    for (int i = 0; i < DuckMain.duckCount.keySet().size(); i++) {
                        Duck thisDuck = DuckMain.duckCount.get(i);
                        if (thisDuck != null && thisDuck.equals(d)) {
                            DuckMain.duckCount.remove(i);
                        }
                    }
                }
                DuckMain.ducks.remove(d);
                e.setQuitMessage("DUCK MODE -> " + ChatColor.YELLOW + Messages.getString("duck") + " " + d.getPlayer().getName() + " " + Messages.getString("leave_message"));
                if (DuckMain.ducks.size() == 0 && DuckMain.state != GameState.LOBBY) {
                    DuckMain.getPlugin().getLogger().info("Server is empty, shutting down...");
                    Bukkit.shutdown();
                }
                break;
            }
        }
        if (DuckMain.spectators.contains(e.getPlayer())) DuckMain.spectators.remove(e.getPlayer());
    }

    @EventHandler
    public void onArrowImpact(ProjectileHitEvent e) {
        e.getEntity().remove();
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        e.setCancelled(true);
        if (!isEmpty(e.getPlayer().getInventory().getItem(4)) || !isEmpty(e.getPlayer().getInventory().getItemInOffHand()))
            return;
        e.getPlayer().getInventory().setItem(4, e.getItem().getItemStack());
        e.getItem().remove();
        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,
                (float) Math.random() - 0.5f);
    }

    private boolean isEmpty(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(Material.AIR);
    }

    @EventHandler
    public void onPlayerChangeSlot(PlayerItemHeldEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) return;
        e.getPlayer().getInventory().setHeldItemSlot(4);
        if (e.getNewSlot() == 0) {
            for (Duck d : DuckMain.ducks) {
                d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_HURT, 10, 1);
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (DuckMain.spectators.contains(e.getWhoClicked())) return;
        e.setCancelled(true);
        e.getWhoClicked().closeInventory();
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.STAINED_GLASS_PANE) || e.getItemDrop().getItemStack().getType().equals(Material.SKULL)) {
            e.setCancelled(true);
        } else {
            if (e.getItemDrop().getItemStack().getItemMeta().getLore() != null &&
                    !e.getItemDrop().getItemStack().getItemMeta().getLore().isEmpty()) {
                if (WeaponWatch.durability.get(e.getItemDrop().getItemStack().getItemMeta().getLore()) != null &&
                        WeaponWatch.durability.get(e.getItemDrop().getItemStack().getItemMeta().getLore()) == 0) {
                    final Item drop = e.getItemDrop();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (drop.isValid() && !(drop.isDead())) {
                                drop.getWorld().playEffect(drop.getLocation(), Effect.EXTINGUISH, 5);
                                drop.getWorld().playEffect(drop.getLocation(), Effect.SMOKE, 10);
                                drop.remove();
                            }
                        }
                    }.runTaskLater(DuckMain.getPlugin(), 40L);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Bukkit.getLogger().info("IvDrgEvt");
        //Prevent from switching into offhand
        event.setCancelled(true);
    }


}
