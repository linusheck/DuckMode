package me.glatteis.duckmode.game;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.setting.SettingDatabase;
import me.glatteis.duckmode.setting.SettingType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DuckLobby implements Listener {

    private boolean activated = false;

    public static void configureLobby() {
        Location sourceLocation = new Location(DuckMain.getWorld(), 1, 23, 5);
        for (int i = 3; i < 8; i++) {
            sourceLocation.setZ(i);
            sourceLocation.getBlock().setType(Material.WALL_SIGN);
            BlockState state = sourceLocation.getBlock().getState();
            org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.WALL_SIGN);
            matSign.setFacingDirection(BlockFace.EAST);
            if (!(state instanceof Sign)) continue;
            Sign s = (Sign) state;
            s.setData(matSign);
            String thisSetting = SettingDatabase.settings.get(i - 3);
            SettingDatabase.settingsSigns.put(s, thisSetting);
            if (thisSetting.equals(SettingType.POINTS_TO_WIN.toString()) || thisSetting.equals(SettingType.ROUNDS.toString())) {
                s.setLine(0, ChatColor.GREEN + thisSetting);
                SettingDatabase.intSetting.put(thisSetting, 0);
                s.setLine(1, ChatColor.GRAY + "==========");
                s.setLine(2, ChatColor.GOLD + String.valueOf(SettingDatabase.switchSettingsFor(thisSetting)));
                s.setLine(3, ChatColor.GRAY + "==========");
            } else if (thisSetting.equals(SettingType.HATS.toString())) {
                s.setLine(0, ChatColor.GREEN + thisSetting);
                s.setLine(2, ChatColor.LIGHT_PURPLE + Messages.getString("hats_description"));
            }
            s.update();
        }
    }

    @EventHandler
    public void signClick(PlayerInteractEvent e) {
        if (DuckMain.state.equals(GameState.LOBBY) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
            Sign s = (Sign) e.getClickedBlock().getState();
            String setting = SettingDatabase.settingsSigns.get(s);
            if (setting == null) return;
            Bukkit.getLogger().info("Setting:" + setting);
            //Because this is Java 1.6, I can't make a setting switch. D:
            if (setting.equals(SettingType.POINTS_TO_WIN.toString()) || setting.equals(SettingType.ROUNDS.toString())) {
                String switchSetting = String.valueOf(SettingDatabase.switchSettingsFor(setting));
                s.setLine(2, ChatColor.GOLD + switchSetting);
                s.update();
            }
            else if (setting.equals(SettingType.HATS.toString())) {
                DuckMain.hats.openHatInventory(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void hatChoice(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Bukkit.getLogger().info("Soosenbinder. " + (e.getInventory().getTitle()) + ", " + SettingType.HATS.toString());
        if (ChatColor.stripColor(e.getInventory().getTitle()).equals(SettingType.HATS.toString())) {
            for (Duck d : DuckMain.ducks) {
                if (d.getPlayer().equals(e.getWhoClicked())) {
                    DuckMain.hats.setHat(d, e.getCurrentItem());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (activated) return;
        if (DuckMain.state.equals(GameState.LOBBY)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.WOOD_BUTTON)
                    && DuckMain.ducks.size() > 0) {
                countdown();
            }
        }
    }

    public void countdown() {
        activated = true;
        new BukkitRunnable() {
            int countdown = 3;
            public void run() {
                for (Duck d : DuckMain.ducks) {
                    DuckReflectionMethods.title(d.getPlayer(), ChatColor.RED.toString() + countdown, 0, 20, 5);
                }
                countdown--;
                if (countdown == 0) {
                    Intermission.getIntermission().create();
                    DuckMain.continueGame.startRound();
                    this.cancel();
                }

            }
        }.runTaskTimer(DuckMain.getPlugin(), 20L, 20L);
    }


}
