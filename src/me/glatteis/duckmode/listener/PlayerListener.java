package me.glatteis.duckmode.listener;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.game.GameState;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.*;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

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
        ChatColor stateColor = (DuckMain.getState().equals(GameState.LOBBY) ? ChatColor.GREEN : ChatColor.RED);
        ChatColor fullColor = (DuckMain.getPlugin().getDucks().size() < DuckMain.getPlugin().getMaxPlayerCount() ? ChatColor.GREEN : ChatColor.RED);
        String pingString = ChatColor.YELLOW + "\\" + ChatColor.UNDERLINE + Messages.getString("duck_mode") + ChatColor.RESET + ChatColor.YELLOW +
                "/ " + fullColor + Bukkit.getServer().getOnlinePlayers().size() + " " + Messages.getString("players_online") + ChatColor.RESET + " || " +
                stateColor + ChatColor.BOLD + DuckMain.getState() + "\n" + ChatColor.GRAY + Messages.getString("motd_description");
        e.setMaxPlayers(DuckMain.getPlugin().getMaxPlayerCount());
        e.setMotd(pingString);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (DuckMain.getPlugin().getContinueGame().canNotMove) {
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
        if (DuckMain.getState().equals(GameState.LOBBY) && (!(Bukkit.getServer().getOnlinePlayers().size() > DuckMain.getPlugin().getMaxPlayerCount()))) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().getInventory().setHeldItemSlot(4);
            Duck d = new Duck(e.getPlayer(), DuckMain.getPlugin().getSpawnLocation());
            DuckMain.getPlugin().getDucks().add(d);
            d.prepareInventory();
            d.disableJumping();
            DuckReflectionMethods.title(e.getPlayer(), ChatColor.RED +
                    (DuckMain.getPlugin().getJoinTitle() != null ? DuckMain.getPlugin().getJoinTitle() : Messages.getString("big_screen_title")), 5, 30, 5);
            DuckReflectionMethods.subtitle(e.getPlayer(), DuckMain.getPlugin().getJoinSubtitle() != null ? DuckMain.getPlugin().getJoinSubtitle() :
                    Messages.getString("version") + " " + DuckMain.getPlugin().getDescription().getVersion(), 5, 30, 5);

            e.getPlayer().teleport(DuckMain.getPlugin().getSpawnLocation());
            e.setJoinMessage("DUCK MODE -> " + ChatColor.YELLOW + Messages.getString("duck") + " " + e.getPlayer().getName() + " " + Messages.getString("join_message"));

            //Init lighting in lobby because that is bugged by default in 1.9.2 >
            //And yes, I tried Chunk#initLighting. Didn't work at all. :(

            new BukkitRunnable() {
                int i = 0;
                Location l = new Location(DuckMain.getWorld(), 7, 21, 9);

                @Override
                public void run() {
                    switch (i) {
                        case 0:
                            l.getBlock().setType(Material.SEA_LANTERN);
                            i = 1;
                            break;
                        case 1:
                            l.getBlock().setType(Material.AIR);
                            cancel();
                            break;
                    }
                }
            }.runTaskTimer(DuckMain.getPlugin(), 5, 1);
            ;

            if (DuckMain.getPlugin().getAutoStart() > 0 && Bukkit.getOnlinePlayers().size() >= DuckMain.getPlugin().getAutoStart()) {
                ListenerActivator.lobbyCountdown();
            }
        } else {
            DuckMain.getPlugin().getSpectators().add(e.getPlayer());
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.setJoinMessage(null);
            e.getPlayer().sendMessage(Messages.getString("spectator.join"));
            if (DuckMain.getState().equals(GameState.LOBBY)) {
                e.getPlayer().teleport(DuckMain.getPlugin().getSpawnLocation());
            } else {
                Vector averageLocation = new Vector();
                int aliveDucks = 0;
                for (Duck d : DuckMain.getPlugin().getDucks()) {
                    aliveDucks++;
                    if (!d.isDead()) {
                        averageLocation = averageLocation.add(d.getPlayer().getLocation().toVector());
                    }
                }
                averageLocation = averageLocation.divide(new Vector(aliveDucks, aliveDucks, aliveDucks));
                e.getPlayer().teleport(averageLocation.toLocation(DuckMain.getWorld()));
            }
        }

        String resourcePackLink = DuckMain.getPlugin().isIndevResourcePack() ?
                "http://glatteis.bplaced.net/DuckMode/resource_pack_indev.zip" :
                "http://glatteis.bplaced.net/DuckMode/rp.php"; //rp.php counts downloads
        e.getPlayer().setResourcePack("http://glatteis.bplaced.net/DuckMode/rp.php?" + new Random().nextInt()); //Random number overrides weird resource pack caching

        //Old resource pack links:
        //https://www.dropbox.com/s/baxsqe7310dwyze/rp_dev.zip?dl=1
        //https://www.dropbox.com/s/z9wbr65n6csvzsq/rp.zip?dl=1
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        for (Duck d : DuckMain.getPlugin().getDucks()) {
            if (d.getPlayer().equals(e.getPlayer())) {
                if (!DuckMain.getPlugin().getDuckCount().isEmpty()) {
                    for (int i = 0; i < DuckMain.getPlugin().getDuckCount().keySet().size(); i++) {
                        Duck thisDuck = DuckMain.getPlugin().getDuckCount().get(i);
                        if (thisDuck != null && thisDuck.equals(d)) {
                            DuckMain.getPlugin().getDuckCount().remove(i);
                        }
                    }
                }
                DuckMain.getPlugin().getDucks().remove(d);
                e.setQuitMessage("DUCK MODE -> " + ChatColor.YELLOW + Messages.getString("duck") + " " + d.getPlayer().getName() + " " + Messages.getString("leave_message"));
                if (DuckMain.getPlugin().getDucks().size() == 0 && DuckMain.getState() != GameState.LOBBY) {
                    DuckMain.getPlugin().getLogger().info("Server is empty, shutting down...");
                    Bukkit.shutdown();
                }
                break;
            }
        }
        if (DuckMain.getPlugin().getSpectators().contains(e.getPlayer())) DuckMain.getPlugin().getSpectators().remove(e.getPlayer());
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
            for (Duck d : DuckMain.getPlugin().getDucks()) {
                d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_HURT, 10, 1);
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (! (e.getWhoClicked() instanceof Player)) return;
        if (DuckMain.getPlugin().getSpectators().contains(e.getWhoClicked())) return;
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
        //Prevent from switching into offhand
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.getBlock().getType() == Material.SOIL && event.getEntity() instanceof Creature)
            event.setCancelled(true);
    }


}
