package me.glatteis.duckmode;

import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        ChatColor stateColor = (DuckMain.state.equals(GameState.LOBBY) ? ChatColor.GREEN : ChatColor.RED);
        ChatColor fullColor = (DuckMain.ducks.size() < DuckMain.maxPlayerCount ? ChatColor.GREEN : ChatColor.RED);
        String pingString = ChatColor.YELLOW + "\\" + ChatColor.UNDERLINE + Messages.getString("duck_mode") + ChatColor.RESET + ChatColor.YELLOW + //$NON-NLS-1$ //$NON-NLS-2$
                "/ " + fullColor + Bukkit.getServer().getOnlinePlayers().size() + Messages.getString("players_online") + ChatColor.RESET + " || " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                stateColor + ChatColor.BOLD + DuckMain.state + "\n" + ChatColor.GRAY + Messages.getString("motd_description"); //$NON-NLS-1$ //$NON-NLS-2$
        e.setMaxPlayers(DuckMain.maxPlayerCount);
        e.setMotd(pingString);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (ContinueGame.canNotMove) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || (e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {
        if (!DuckMain.state.equals(GameState.LOBBY) || (Bukkit.getServer().getOnlinePlayers().size() > 3)) {
            e.setLoginResult(org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_FULL);
            e.setKickMessage(ChatColor.RED + Messages.getString("game_started_or_full")); //$NON-NLS-1$
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (DuckMain.state.equals(GameState.LOBBY) && (!(Bukkit.getServer().getOnlinePlayers().size() > DuckMain.maxPlayerCount))) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().getInventory().setHeldItemSlot(4);
            Location loc = DuckMain.spawnLocation;
            Duck d = new Duck(e.getPlayer(), loc);
            DuckMain.ducks.add(d);
            e.getPlayer().teleport(loc);
            StaticMethods.prepareInventory(d);
            StaticMethods.disableJumping(e.getPlayer());
            DuckReflectionMethods.title(e.getPlayer(), ChatColor.RED + Messages.getString("big_screen_title"), 5, 30, 5); //$NON-NLS-1$
            DuckReflectionMethods.subtitle(e.getPlayer(), Messages.getString("version") + DuckMain.getPlugin().getDescription().getVersion(), 5, 30, 5); //$NON-NLS-1$

            e.setJoinMessage("DUCK MODE ->" + ChatColor.YELLOW + Messages.getString("duck") + e.getPlayer().getName() + Messages.getString("join_message")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        }

        e.getPlayer().setResourcePack(DuckMain.indevResourcePack ? "https://www.dropbox.com/s/baxsqe7310dwyze/rp_dev.zip?dl=1" : "https://www.dropbox.com/s/z9wbr65n6csvzsq/rp.zip?dl=1"); //$NON-NLS-1$ //$NON-NLS-2$
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
                e.setQuitMessage("DUCK MODE -> " + ChatColor.YELLOW + Messages.getString("duck_2") + d.getPlayer().getName() + Messages.getString("leave_message")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                if (DuckMain.ducks.size() == 0 && DuckMain.state != GameState.LOBBY) {
                    Bukkit.shutdown();
                }
                break;
            }
        }
    }

    @EventHandler
    public void onArrowImpact(ProjectileHitEvent e) {
        e.getEntity().remove();
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (e.getPlayer().getInventory().getItem(4) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChangeSlot(PlayerItemHeldEvent e) {
        e.getPlayer().getInventory().setHeldItemSlot(4);
        if (e.getNewSlot() == 0) {
            for (Duck d : DuckMain.ducks) {
                d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.CHICKEN_HURT, 10, 1);
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        e.getWhoClicked().closeInventory();
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getType().equals(Material.STAINED_GLASS_PANE) || e.getItemDrop().equals(Material.SKULL)) {
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

}
